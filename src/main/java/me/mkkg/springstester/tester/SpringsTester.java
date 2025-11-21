package main.java.me.mkkg.springstester.tester;

import main.java.me.mkkg.springstester.tester.cards.*;

import javax.swing.*;
import java.awt.*;

public class SpringsTester {

    public static final Dimension APP_SIZE = new Dimension(600, 1000);

    private JPanel menuPanel;

    private TestingCard testingCard;
    private ResultsCard resultsCard;
    private AboutCard aboutCard;

    private Card currentlyDisplayedCard;

    private static SpringsTester instance;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());

        SpringsTester inst = new SpringsTester();
        SpringsTester.instance = inst;

        GridBagConstraints c = new GridBagConstraints();

        MenuPanel menuPanelInst = new MenuPanel();
        inst.testingCard = new TestingCard();
        inst.resultsCard = new ResultsCard();
        inst.aboutCard = new AboutCard();

        inst.menuPanel = menuPanelInst.createPanel();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0;
        frame.add(inst.menuPanel, c);

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        frame.add(inst.testingCard.getPanel(), c);
        frame.add(inst.resultsCard.getPanel(), c);
        frame.add(inst.aboutCard.getPanel(), c);

        inst.currentlyDisplayedCard = inst.testingCard;
        inst.currentlyDisplayedCard.getPanel().setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Springs Tester");
        frame.setSize(APP_SIZE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @SuppressWarnings("unused")
    public JPanel getMenuPanel() {
        return menuPanel;
    }

    public TestingCard getTestingCard() {
        return testingCard;
    }

    public ResultsCard getResultsCard() {
        return resultsCard;
    }

    public AboutCard getAboutCard() {
        return aboutCard;
    }

    public Card getCurrentlyDisplayedCard() {
        return currentlyDisplayedCard;
    }

    public void setCurrentlyDisplayedCard(Card newCard) {
        this.currentlyDisplayedCard = newCard;
    }

    public static SpringsTester getInstance() {
        return instance;
    }
}