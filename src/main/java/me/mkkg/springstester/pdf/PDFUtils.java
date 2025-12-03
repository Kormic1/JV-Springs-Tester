package main.java.me.mkkg.springstester.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PDFUtils {

    public static final String LOAD_BEARING_POPE = "jp2.jpg";

    public static final int FONT_SIZE_TITLE = 18;
    public static final int FONT_SIZE_HEADER = 12;
    public static final int FONT_SIZE_BODY = 10;

    public static final float ROW_HEIGHT = 20f;
    public static final float CELL_MARGIN = 5f;

    public static float drawDataTable(PDPageContentStream cs, float pageWidth, float margin, float y,
                                      String[] headers, Object[] values,
                                      PDType0Font fontBold, PDType0Font fontRegular) throws IOException {

        float tableTopY = y;
        float tableBottomY = y - (ROW_HEIGHT * 2);
        float headerRowY = y - ROW_HEIGHT;
        float valueRowY = y - (ROW_HEIGHT * 2);

        int colCount = headers.length;
        float cellWidth = pageWidth / colCount;

        // Tła
        cs.setNonStrokingColor(Color.LIGHT_GRAY);
        cs.addRect(margin, headerRowY, pageWidth, ROW_HEIGHT);
        cs.fill();
        cs.setNonStrokingColor(Color.WHITE);
        cs.addRect(margin, valueRowY, pageWidth, ROW_HEIGHT);
        cs.fill();

        // Linie
        cs.setStrokingColor(Color.BLACK);
        cs.setLineWidth(0.5f);
        cs.addRect(margin, tableBottomY, pageWidth, ROW_HEIGHT * 2);
        cs.stroke();
        cs.moveTo(margin, headerRowY);
        cs.lineTo(margin + pageWidth, headerRowY);
        cs.stroke();

        float currentX = margin + cellWidth;
        for (int i = 1; i < colCount; i++) {
            cs.moveTo(currentX, tableTopY);
            cs.lineTo(currentX, tableBottomY);
            cs.stroke();
            currentX += cellWidth;
        }

        // Tekst
        cs.setFont(fontBold, FONT_SIZE_HEADER);
        cs.setNonStrokingColor(Color.BLACK);
        float textX = margin + CELL_MARGIN;
        float textY_header = headerRowY + CELL_MARGIN + 2;

        for (String header : headers) {
            cs.beginText();
            cs.newLineAtOffset(textX, textY_header);
            cs.showText(header);
            cs.endText();
            textX += cellWidth;
        }

        cs.setFont(fontRegular, FONT_SIZE_BODY);
        textX = margin + CELL_MARGIN;
        float textY_value = valueRowY + CELL_MARGIN + 2;

        for (Object value : values) {
            cs.beginText();
            cs.newLineAtOffset(textX, textY_value);
            cs.showText(value.toString());
            cs.endText();
            textX += cellWidth;
        }

        return tableBottomY;
    }


    public static float drawStaticImage(PDDocument document, PDPageContentStream cs, PDRectangle mediaBox, float margin) throws IOException {

        PDImageXObject pdImage;
        try {
            InputStream is = PDFUtils.class.getResourceAsStream(LOAD_BEARING_POPE);
            if (is == null) {
                System.err.println("BŁĄD: Nie można znaleźć obrazka: " + LOAD_BEARING_POPE);
                return 0f;
            }

            BufferedImage bimg = ImageIO.read(is);
            if (bimg == null) {
                throw new IOException("Nie można odczytać pliku obrazka.");
            }

            pdImage = LosslessFactory.createFromImage(document, bimg);
            is.close();

        } catch (Exception e) {
            System.err.println("Błąd podczas wczytywania statycznego obrazka: " + e.getMessage());
            e.printStackTrace();
            return 0f;
        }

        float imageWidth = pdImage.getWidth();
        float imageHeight = pdImage.getHeight();
        float maxWidth = 200f;

        float scale = 1.0f;
        if (imageWidth > maxWidth) {
            scale = maxWidth / imageWidth;
        }

        float scaledWidth = imageWidth * scale;
        float scaledHeight = imageHeight * scale;

        float x = margin + (mediaBox.getWidth() - 2 * margin - scaledWidth) / 2;
        float y = mediaBox.getLowerLeftY() + margin;

        cs.drawImage(pdImage, x, y, scaledWidth, scaledHeight);

        return scaledHeight;
    }

    public static void drawDynamicImage(PDDocument document, PDPageContentStream cs, PDRectangle mediaBox, float margin, BufferedImage bimg, float yPosition) throws IOException {
        if (bimg == null) return;

        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bimg);

        float imageWidth = pdImage.getWidth();
        float imageHeight = pdImage.getHeight();
        float maxWidth = 250f;

        float scale = 1.0f;
        if (imageWidth > maxWidth) {
            scale = maxWidth / imageWidth;
        }

        float scaledWidth = imageWidth * scale;
        float scaledHeight = imageHeight * scale;

        float x = margin + (mediaBox.getWidth() - 2 * margin - scaledWidth) / 2;

        cs.drawImage(pdImage, x, yPosition, scaledWidth, scaledHeight);
    }
}