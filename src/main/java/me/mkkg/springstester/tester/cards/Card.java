package main.java.me.mkkg.springstester.tester.cards;

import javax.swing.*;

public class Card {

    protected final JPanel panel;
    private static Card instance;

    public Card() {
        instance = this;

        panel = new JPanel();
        panel.setVisible(false);
    }

    public JPanel getPanel() {
        return panel;
    }

    public static Card getInstance() {
        return instance;
    }
}
