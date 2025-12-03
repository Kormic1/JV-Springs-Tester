package main.java.me.mkkg.springstester.database;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageBase64Handler {


    public static String encode(Image image) {
        if (image == null) {
            return null;
        }

        BufferedImage bufferedImage = toBufferedImage(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("Błąd podczas kodowania obrazu do Base64: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static Image decode(String base64image) {
        if (base64image == null || base64image.isEmpty()) {
            return null;
        }

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64image);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(inputStream);
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Błąd podczas dekodowania obrazu z Base64: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}