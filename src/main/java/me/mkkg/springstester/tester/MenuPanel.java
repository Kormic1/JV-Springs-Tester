package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import java.awt.*;

public class MenuPanel {

    private static final Color BUTTONS_FOREGROUND_COLOR = new Color(0, 0, 0);
    private static final Color BUTTONS_BACKGROUND_COLOR = new Color(200, 200, 200);

    private JButton testButton;
    private JButton resultsButton;
    private JButton aboutButton;

    private static MenuPanel instance;

    public JPanel createPanel() {

        MenuPanel inst = new MenuPanel();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 230, 230));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setVisible(true);

        testButton = new JButton("Run Test");
        testButton.setForeground(BUTTONS_FOREGROUND_COLOR);
        testButton.setBackground(BUTTONS_BACKGROUND_COLOR);
        testButton.setFocusable(false);
        panel.add(testButton);
        inst.testButton = testButton;

        resultsButton = new JButton("Results List");
        resultsButton.setForeground(BUTTONS_FOREGROUND_COLOR);
        resultsButton.setBackground(BUTTONS_BACKGROUND_COLOR);
        resultsButton.setFocusable(false);
        panel.add(resultsButton);
        inst.resultsButton = resultsButton;

        aboutButton = new JButton("About");
        aboutButton.setForeground(BUTTONS_FOREGROUND_COLOR);
        aboutButton.setBackground(BUTTONS_BACKGROUND_COLOR);
        aboutButton.setFocusable(false);
        panel.add(aboutButton);
        inst.aboutButton = aboutButton;

        MenuPanel.instance = inst;

        return panel;
    }

    public void changePanel(JPanel panel) {

        SpringsTester springsTester = SpringsTester.getInstance();

        if (!springsTester.getCurrentlyDisplayedPanel().equals(panel)) {
            springsTester.getCurrentlyDisplayedPanel().setVisible(false);
            springsTester.setCurrentlyDisplayedPanel(panel);
            springsTester.getCurrentlyDisplayedPanel().setVisible(true);
        }
    }

    public void registerEvents() {

        SpringsTester springsTester = SpringsTester.getInstance();
        MenuPanel inst = MenuPanel.getInstance();

        inst.testButton.addActionListener(e -> changePanel(springsTester.getTestPanel()));
        inst.resultsButton.addActionListener(e -> changePanel(springsTester.getResultsPanel()));
        inst.aboutButton.addActionListener(e -> changePanel(springsTester.getAboutPanel()));
    }

    public JButton getTestButton() {
        return instance.testButton;
    }

    public JButton getResultsButton() {
        return instance.resultsButton;
    }

    public JButton getAboutButton() {
        return instance.aboutButton;
    }

    public static MenuPanel getInstance() {
        return instance;
    }
}
