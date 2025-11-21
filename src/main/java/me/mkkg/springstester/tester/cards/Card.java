package main.java.me.mkkg.springstester.tester.cards;

import javax.swing.*;
import java.awt.*;

public class Card {

    protected static final Color BG_COLOR = new Color(200, 200, 200);

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

    @SuppressWarnings("unused")
    public static Card getInstance() {
        return instance;
    }
}
