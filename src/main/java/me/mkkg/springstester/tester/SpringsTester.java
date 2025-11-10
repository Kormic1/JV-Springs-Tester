package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import java.awt.*;

public class SpringsTester {

    public static final int APP_WIDTH = 600;
    public static final int APP_HEIGHT = 1000;

    private JPanel menuPanel;
    private JPanel testPanel;
    private JPanel resultsPanel;
    private JPanel aboutPanel;

    private JPanel currentlyDisplayedPanel;

    private static SpringsTester instance;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());

        System.out.println("Test");

        SpringsTester inst = new SpringsTester();

        GridBagConstraints c = new GridBagConstraints();

        MenuPanel menuPanelInst = new MenuPanel();
        TestPanel testPanelInst = new TestPanel();
        ResultsPanel resultsPanelInst = new ResultsPanel();
        AboutPanel aboutPanelInst = new AboutPanel();

        inst.menuPanel = menuPanelInst.createPanel();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        frame.add(inst.menuPanel, c);

        inst.testPanel = testPanelInst.createPanel();
        inst.resultsPanel = resultsPanelInst.createPanel();
        inst.aboutPanel = aboutPanelInst.createPanel();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        frame.add(inst.testPanel, c);
        frame.add(inst.resultsPanel, c);
        frame.add(inst.aboutPanel, c);

        inst.currentlyDisplayedPanel = inst.testPanel;

        SpringsTester.instance = inst;

        menuPanelInst.registerEvents();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Springs Tester");
        frame.setSize(APP_WIDTH, APP_HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    public JPanel getTestPanel() {
        return testPanel;
    }

    public JPanel getResultsPanel() {
        return resultsPanel;
    }

    public JPanel getAboutPanel() {
        return aboutPanel;
    }

    public JPanel getCurrentlyDisplayedPanel() {
        return currentlyDisplayedPanel;
    }

    public void setCurrentlyDisplayedPanel(JPanel currentlyDisplayedPanel) {
        this.currentlyDisplayedPanel = currentlyDisplayedPanel;
    }

    public static SpringsTester getInstance() {
        return instance;
    }
}