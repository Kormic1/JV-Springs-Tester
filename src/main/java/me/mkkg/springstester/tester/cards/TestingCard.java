package main.java.me.mkkg.springstester.tester.cards;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TestingCard extends Card {

    private static final Color BG_COLOR = new Color(150, 150, 150);

    public TestingCard() {
        panel.setBackground(BG_COLOR);
    }
    //testing purposes
    public static BufferedImage TestIm = loadImage("jp2.jpg");

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(TestingCard.class.getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
