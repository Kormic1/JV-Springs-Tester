package main.java.me.mkkg.springstester.tester;

import main.java.me.mkkg.springstester.tester.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class MenuPanel {

    private static final Color BG_COLOR = new Color(220, 220, 220);
    private static final Color BUTTONS_FG_COLOR = new Color(0, 0, 0);
    private static final Color BUTTONS_BG_COLOR = new Color(150, 150, 150);
    private static final Color ACTIVE_BUTTONS_BG_COLOR = new Color(200, 200, 200);

    private JButton testButton;
    private JButton resultsButton;
    private JButton aboutButton;

    private final List<JButton> cardButtons = new ArrayList<>();

    private static MenuPanel instance;

    public JPanel createPanel() {

        MenuPanel inst = new MenuPanel();

        JPanel panel = new JPanel();
        panel.setBackground(BG_COLOR);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setVisible(true);

        testButton = createCardButton("Run Test");
        panel.add(testButton);
        inst.cardButtons.add(testButton);
        inst.testButton = testButton;

        resultsButton = createCardButton("Test Results");
        panel.add(resultsButton);
        inst.cardButtons.add(resultsButton);
        inst.resultsButton = resultsButton;

        aboutButton = createCardButton("About");
        panel.add(aboutButton);
        inst.cardButtons.add(aboutButton);
        inst.aboutButton = aboutButton;

        MenuPanel.instance = inst;

        registerEvents();
        testButton.doClick();

        return panel;
    }

    private void setButtonActive(JButton button) {
        SpringsTester tester = SpringsTester.getInstance();
        if (tester.isTestInProgress()) return;

        for (JButton b : getInstance().cardButtons) {
            if (b.equals(button)) {
                button.setBackground(ACTIVE_BUTTONS_BG_COLOR);
                continue;
            }

            b.setBackground(BUTTONS_BG_COLOR);
        }
    }

    public void changeCard(Card card) {
        SpringsTester tester = SpringsTester.getInstance();
        if (tester.isTestInProgress()) return;

        if (!tester.getCurrentlyDisplayedCard().equals(card)) {
            tester.getCurrentlyDisplayedCard().getPanel().setVisible(false);
            tester.setCurrentlyDisplayedCard(card);
            tester.getCurrentlyDisplayedCard().getPanel().setVisible(true);
        }
    }

    public void registerEvents() {

        SpringsTester springsTester = SpringsTester.getInstance();
        MenuPanel inst = MenuPanel.getInstance();

        inst.testButton.addActionListener(e -> {
            changeCard(springsTester.getTestingCard());
            setButtonActive(inst.testButton);
        });
        inst.resultsButton.addActionListener(e -> {
            changeCard(springsTester.getResultsCard());
            setButtonActive(inst.resultsButton);
        });
        inst.aboutButton.addActionListener(e -> {
            changeCard(springsTester.getAboutCard());
            setButtonActive(inst.aboutButton);
        });
    }

    private JButton createCardButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(BUTTONS_FG_COLOR);
        button.setBackground(BUTTONS_BG_COLOR);
        button.setFocusable(false);
        return button;
    }

    @SuppressWarnings("unused")
    public JButton getTestButton() {
        return this.testButton;
    }

    @SuppressWarnings("unused")
    public JButton getResultsButton() {
        return this.resultsButton;
    }

    @SuppressWarnings("unused")
    public JButton getAboutButton() {
        return this.aboutButton;
    }

    public static MenuPanel getInstance() {
        return instance;
    }
}
