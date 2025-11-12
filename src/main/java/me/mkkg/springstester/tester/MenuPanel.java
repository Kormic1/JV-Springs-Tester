package main.java.me.mkkg.springstester.tester;

import main.java.me.mkkg.springstester.tester.cards.Card;
import main.java.me.mkkg.springstester.tester.cards.TestingCard; // Import nowej karty

import javax.swing.*;
import java.awt.*;

public class MenuPanel {

    private static final Color BG_COLOR = new Color(220, 220, 220);
    private static final Color BUTTONS_FG_COLOR = new Color(0, 0, 0);
    private static final Color BUTTONS_BG_COLOR = new Color(200, 200, 200);

    private JButton testButton;
    private JButton resultsButton;
    private JButton aboutButton;

    private static MenuPanel instance;

    public JPanel createPanel() {


        MenuPanel inst = this;

        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setVisible(true);

        testButton = new JButton("Run Test");
        testButton.setForeground(BUTTONS_FG_COLOR);
        testButton.setBackground(BUTTONS_BG_COLOR);
        testButton.setFocusable(false);
        panel.add(testButton);
        inst.testButton = testButton;

        resultsButton = new JButton("Test Results");
        resultsButton.setForeground(BUTTONS_FG_COLOR);
        resultsButton.setBackground(BUTTONS_BG_COLOR);
        resultsButton.setFocusable(false);
        panel.add(resultsButton);
        inst.resultsButton = resultsButton;

        aboutButton = new JButton("About");
        aboutButton.setForeground(BUTTONS_FG_COLOR);
        aboutButton.setBackground(BUTTONS_BG_COLOR);
        aboutButton.setFocusable(false);
        panel.add(aboutButton);
        inst.aboutButton = aboutButton;

        MenuPanel.instance = inst;


        return panel;
    }

    public void changeCard(Card card) {
        SpringsTester springsTester = SpringsTester.getInstance();

        if (!springsTester.getCurrentlyDisplayedCard().equals(card)) {
            springsTester.getCurrentlyDisplayedCard().getPanel().setVisible(false);
            springsTester.setCurrentlyDisplayedCard(card);
            springsTester.getCurrentlyDisplayedCard().getPanel().setVisible(true);
        }
    }

    public void registerEvents() {
        SpringsTester springsTester = SpringsTester.getInstance();
        MenuPanel inst = MenuPanel.getInstance();


        inst.testButton.addActionListener(e -> {
            changeCard(springsTester.getTestingCard());

            TestingCard testingCardInstance = TestingCard.getInstance();
            if (testingCardInstance != null) {
                testingCardInstance.refreshTableData();
            } else {
                System.err.println("Błąd: testingCardInstance jest null!");
            }
        });


        inst.resultsButton.addActionListener(e -> changeCard(springsTester.getResultsCard()));
        inst.aboutButton.addActionListener(e -> changeCard(springsTester.getAboutCard()));
    }

    public JButton getTestButton() {
        return this.testButton;
    }

    public JButton getResultsButton() {
        return this.resultsButton;
    }

    public JButton getAboutButton() {
        return this.aboutButton;
    }

    public static MenuPanel getInstance() {
        return instance;
    }
}
