package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import java.awt.*;

public class AboutPanel {

    private static AboutPanel instance;

    public JPanel createPanel() {

        AboutPanel inst = new AboutPanel();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(100, 100, 100));
        panel.setVisible(false);

        AboutPanel.instance = inst;

        return panel;
    }

    public static AboutPanel getInstance() {
        return instance;
    }
}
