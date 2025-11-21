package main.java.me.mkkg.springstester.tester.cards;

import main.java.me.mkkg.springstester.tester.graph.Graph;
import main.java.me.mkkg.springstester.tester.graph.GraphOptions;
import main.java.me.mkkg.springstester.tester.graph.GraphUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;

import static main.java.me.mkkg.springstester.tester.utils.Utils.delay;

public class TestingCard extends Card {

    private static final Color BG_COLOR = new Color(200, 200, 200);

    public TestingCard() {
        panel.setBackground(BG_COLOR);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        GraphOptions graphOptions = new GraphOptions();
        graphOptions.title = "Charakterystyka zależności siły od przesunięcia";
        graphOptions.xLabel = "Przesunięcie, mm";
        graphOptions.yLabel = "Siła, kN";

        Graph graph = new Graph(graphOptions);
        panel.add(graph);

        new Thread(() -> {
            delay(3000);

            // Generate linear data with noise
            for (int i = 0; i < 100; i++) {
                double x = 0.5 * i + 0.3 * Math.random();
                double y = 0.6 * x + 0.1 * Math.random();
                graph.addPoint(new Point2D.Double(x, y));
                graph.repaintGraph();

                delay(200);
            }

            GraphUtils.saveGraphAsPNG(graph, new File("src/test/resources/test.png"));
        }).start();
    }
}
