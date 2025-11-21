package main.java.me.mkkg.springstester.tester.graph;

import java.awt.*;

public class GraphOptions {
    public Dimension graphSize = new Dimension(580, 400);
    public String title = "Graph";
    public String xLabel = "X Label";
    public String yLabel = "Y Label";
    public Color dataColor = new Color(80, 120, 255);
    public int numOfXTicks = 11;
    public int numOfYTicks = 7;
    public String ticksFormat = "%.1f";
    public boolean sameMargins = true;
    public double marginsPart = 0.1;
    public Font titleAxesFont = new Font("SansSerif", Font.BOLD, 14);
    public Font ticksFont = new Font("SansSerif", Font.PLAIN, 12);
}