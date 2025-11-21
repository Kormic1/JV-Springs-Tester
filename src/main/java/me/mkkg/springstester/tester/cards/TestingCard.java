package main.java.me.mkkg.springstester.tester.cards;

import main.java.me.mkkg.springstester.tester.SpringsTester;
import main.java.me.mkkg.springstester.tester.graph.Graph;
import main.java.me.mkkg.springstester.tester.graph.GraphOptions;
import main.java.me.mkkg.springstester.tester.graph.GraphUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;

import static main.java.me.mkkg.springstester.tester.utils.Utils.delay;

public class TestingCard extends Card {

    private static final String GRAPH_TITLE = "Charakterystyka zależności siły od przesunięcia";
    private static final String GRAPH_XLABEL = "Przesunięcie, mm";
    private static final String GRAPH_YLABEL = "Siła, kN";

    public TestingCard() {
        panel.setBackground(BG_COLOR);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        Graph graph = createTestGraph();
        panel.add(graph);

        SpringsTester tester = SpringsTester.getInstance();

        new Thread(() -> {
            delay(3000);

            if(!tester.isTestInProgress()) tester.setTestInProgress(true);

            // Generate linear data with noise
            for (int i = 0; i < 100; i++) {
                double x = 0.5 * i + 0.5 * Math.random();
                double y = 0.6 * x + 0.3 * Math.random();
                graph.addPoint(new Point2D.Double(x, y));
                graph.repaintGraph();

                delay(200);
            }

            GraphUtils.saveGraphAsPNG(graph, new File("src/test/resources/test.png"));
            tester.setTestInProgress(false);
        }).start();
    }

    private Graph createTestGraph() {
        GraphOptions graphOptions = new GraphOptions();
        graphOptions.title = GRAPH_TITLE;
        graphOptions.xLabel = GRAPH_XLABEL;
        graphOptions.yLabel = GRAPH_YLABEL;

        return new Graph(graphOptions);
    }
}
