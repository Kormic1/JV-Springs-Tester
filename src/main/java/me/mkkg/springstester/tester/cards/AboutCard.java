package main.java.me.mkkg.springstester.tester.cards;

import main.java.me.mkkg.springstester.tester.ui.JParagraph;

import javax.swing.*;
import java.awt.*;

public class AboutCard extends Card {

    private static final String descriptionText =
            """
                    Program SpringsTester symuluje proces testowania sprężyn kolejowych. Została w nim zaimplementowana \
                    wizualizacja procesu w postaci animowanego obrazka, a także wykresu charakterystyki pracy sprężyny.
                    
                    W karcie testów możliwy jest wybór kilku typów sprężyn, kontrola wysunięcia platformy, na której \
                    umieszczana jest sprężyna, uruchamianie i zatrzymanie testu oraz kontrola danych procesowych w czasie \
                    rzeczywistym.
                    
                    W karcie raportów wyświetlane są wyniki wszystkich wykonanych testów. Wyniki można filtrować według \
                    kilku parametrów, a także możliwe jest sortowanie według identyfikatorów tekstu i dat wykonania, \
                    rosnąco i malejąco.""";

    private static final String authorsText =
            """
                    Autorzy programu:
                     - Michał Kordaszewski (email: <a href='mailto:mk306319@student.polsl.pl'>mk306319@student.polsl.pl</a>)
                     - Krystian Golus (email: <a href='mailto:kg306260@student.polsl.pl'>kg306260@student.polsl.pl</a>)""";

    private static final String librariesText =
            """
                    Wykorzystane biblioteki:
                     - Apache PDFBox - <a href='https://pdfbox.apache.org/'>https://pdfbox.apache.org/</a>.""";

    public AboutCard() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        panel.setBackground(BG_COLOR);

        JParagraph description = new JParagraph(descriptionText);
        description.setBackground(BG_COLOR);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.weighty = 0;
        c.insets = new Insets(20, 40, 20, 40);
        panel.add(description, c);

        JParagraph authors = new JParagraph(authorsText);
        authors.setBackground(BG_COLOR);
        c.gridy = 1;
        panel.add(authors, c);

        JParagraph libraries = new JParagraph(librariesText);
        libraries.setBackground(BG_COLOR);
        c.gridy = 2;
        panel.add(libraries, c);

        // Empty component to take the remaining vertical space
        c.gridy = 3;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        panel.add(new JLabel(), c);
    }

}