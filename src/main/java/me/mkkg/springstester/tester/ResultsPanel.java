package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import java.awt.*;

public class ResultsPanel {

    private static ResultsPanel instance;

    public JPanel createPanel() {

        ResultsPanel inst = new ResultsPanel();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(150, 150, 150));
        panel.setVisible(false);

        ResultsPanel.instance = inst;

        return panel;
    }

    public static ResultsPanel getInstance() {
        return instance;
    }
}
