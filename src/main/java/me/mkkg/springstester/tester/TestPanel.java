package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import java.awt.*;

public class TestPanel {

    private static TestPanel instance;

    public JPanel createPanel() {

        TestPanel inst = new TestPanel();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(200, 200, 200));
        panel.setVisible(true);

        TestPanel.instance = inst;

        return panel;
    }

    public static TestPanel getInstance() {
        return instance;
    }
}
