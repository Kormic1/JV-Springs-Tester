package main.java.me.mkkg.springstester.tester.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;

public class Graph extends JPanel {

    private static final double TICK_LENGTH_TO_WIDTH_RATIO = 0.01;

    private final GraphOptions graphOptions;

    private int marginX, marginY;

    private List<Point2D.Double> graphData = new ArrayList<>();
    private Graphics2D g2d;

    @SuppressWarnings("unused")
    public Graph() {
        this.graphOptions = new GraphOptions();
        initGraph();
    }

    @SuppressWarnings("unused")
    public Graph(GraphOptions graphOptions) {
        this.graphOptions = graphOptions;
        initGraph();
    }

    @SuppressWarnings("unused")
    public Graph(List<Point2D.Double> graphData, GraphOptions graphOptions) {
        this.graphData = graphData;
        this.graphOptions = graphOptions;
        initGraph();
    }

    public void addPoint(Point2D.Double point) {
        graphData.add(point);
    }

    public void repaintGraph() {
        repaint();
    }

    @SuppressWarnings("unused")
    public void clearGraph() {
        graphData.clear();
        repaintGraph();
    }

    private void initGraph() {
        setPreferredSize(graphOptions.graphSize);
        updateMargins(graphOptions.graphSize);
    }

    private void updateMargins(Dimension dimension) {
        GraphOptions go = graphOptions;
        marginX = (int) (go.marginsPart * dimension.width);
        marginY = go.sameMargins ? marginX : (int) (go.marginsPart * dimension.height);
    }

    private void drawTitle() {
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(graphOptions.title);

        int xPos = getWidth() / 2 - titleWidth / 2;
        int yPos = marginY / 2 + fm.getAscent() / 2;

        g2d.drawString(graphOptions.title, xPos, yPos);
    }

    private void drawAxesLabels() {
        FontMetrics fm = g2d.getFontMetrics();
        int xLabelWidth = fm.stringWidth(graphOptions.xLabel);

        int ticksLength = (int) (TICK_LENGTH_TO_WIDTH_RATIO * getWidth());

        int xLabelXPos = getWidth() / 2 - xLabelWidth / 2;
        int xLabelYPos = getHeight() - marginY / 2 + fm.getAscent() / 2 + ticksLength;

        g2d.drawString(graphOptions.xLabel, xLabelXPos, xLabelYPos);

        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(-Math.PI / 2);

        // X becomes Y, Y becomes -X
        int yLabelXPos = -(getHeight() / 2 + fm.getAscent() / 2);
        int yLabelYPos = marginX / 2 - ticksLength;

        g2d.drawString(graphOptions.yLabel, yLabelXPos, yLabelYPos);

        g2d.setTransform(oldTransform);
    }

    public void drawAxesTicks() {
        if (graphData == null || graphData.size() < 2) {
            return;
        }

        FontMetrics fm = g2d.getFontMetrics();
        double[] dataBounds = GraphUtils.getDataBounds(graphData);
        double maxX = dataBounds[0];
        double maxY = dataBounds[1];

        int ticksLength = (int) (TICK_LENGTH_TO_WIDTH_RATIO * getWidth());

        // X-axis ticks
        int xAxisXPos = getHeight() - marginY;
        double xInterval = (double) (getWidth() - 2 * marginX) / graphOptions.numOfXTicks;

        int xTicksIter = 1;
        for (double x = marginX; x <= getWidth() - marginX; x += xInterval) {
            int intX = (int) x;
            //noinspection SuspiciousNameCombination
            g2d.drawLine(intX, xAxisXPos, intX, xAxisXPos + ticksLength);

            double xTickValue = maxX * (xTicksIter - 1) / graphOptions.numOfXTicks;
            String xTickLabel = String.format(graphOptions.ticksFormat, xTickValue);
            int labelWidth = fm.stringWidth(xTickLabel);

            int xPos = intX - labelWidth / 2;
            int yPos = getHeight() - marginY + fm.getAscent() + ticksLength;

            g2d.drawString(xTickLabel, xPos, yPos);

            xTicksIter++;
        }

        // Y-axis ticks
        double yInterval = (double) (getHeight() - 2 * marginY) / graphOptions.numOfYTicks;

        int yTicksIter = 1;
        for (double y = getHeight() - marginY; y >= marginY; y -= yInterval) {
            int intY = (int) y;
            g2d.drawLine(marginX - ticksLength, intY, marginX, intY);

            double yTickValue = maxY * (yTicksIter - 1) / graphOptions.numOfYTicks;
            String yTickLabel = String.format(graphOptions.ticksFormat, yTickValue);
            int labelWidth = fm.stringWidth(yTickLabel);

            int xPos = marginX - labelWidth - ticksLength;
            int yPos = intY + fm.getAscent() / 2;

            g2d.drawString(yTickLabel, xPos, yPos);

            yTicksIter++;
        }
    }

    private void drawData(int marginX, int marginY, List<Point2D.Double> data) {
        if (data == null || data.size() < 2) {
            return;
        }

        Point origin = new Point(marginX, getHeight() - marginY);

        double[] dataBounds = GraphUtils.getDataBounds(data);
        double maxX = dataBounds[0];
        double maxY = dataBounds[1];

        double scaleX = (double) (getWidth() - 2 * marginX) / maxX;
        double scaleY = (double) (getHeight() - 2 * marginY) / maxY;

        g2d.setColor(graphOptions.dataColor);

        for (int i = 0; i < data.size() - 1; i++) {
            Point2D.Double currentPoint = GraphUtils.nonNegativePoint(data.get(i));
            Point2D.Double nextPoint = GraphUtils.nonNegativePoint(data.get(i + 1));

            int x1 = (int) (origin.x + currentPoint.x * scaleX);
            int y1 = (int) (origin.y - currentPoint.y * scaleY);

            int x2 = (int) (origin.x + nextPoint.x * scaleX);
            int y2 = (int) (origin.y - nextPoint.y * scaleY);

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        g2d.setFont(graphOptions.titleAxesFont);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawTitle();
        drawAxesLabels();

        g2d.setFont(graphOptions.ticksFont);

        drawAxesTicks();

        int width = getWidth();
        int height = getHeight();

        Point origin = new Point(marginX, height - marginY);

        // X axis
        g2d.drawLine(origin.x, origin.y, width - marginX, origin.y);
        // Y axis
        g2d.drawLine(origin.x, origin.y, origin.x, marginY);

        drawData(marginX, marginY, graphData);
    }
}