package main.java.me.mkkg.springstester.tester.graph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GraphUtils {

    public static Point2D.Double nonNegativePoint(Point2D.Double point) {
        if (point.x < 0) point.x = 0;
        if (point.y < 0) point.y = 0;
        return point;
    }

    public static double[] getDataBounds(List<Point2D.Double> data) {
        double maxX = data.stream().mapToDouble(p -> p.x).max().orElse(Double.NaN);
        double maxY = data.stream().mapToDouble(p -> p.y).max().orElse(Double.NaN);

        //noinspection ExpressionComparedToItself
        if (maxX != maxX || maxY != maxY) {
            // NaN compared with itself returns false
            throw new RuntimeException();
        }

        return new double[]{maxX, maxY};
    }

    public static void saveGraphAsPNG(Graph graph, File path) {
        Dimension size = graph.getSize();
        if (size.width == 0 || size.height == 0) {
            graph.setSize(graph.getPreferredSize());
            size = graph.getSize();
        }

        BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        graph.paint(g2);
        g2.dispose();

        try {
            ImageIO.write(img, "png", path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not save graph to " + path, "Error", JOptionPane.ERROR_MESSAGE);
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}