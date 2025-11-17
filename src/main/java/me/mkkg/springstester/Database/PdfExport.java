package main.java.me.mkkg.springstester.Database;

// Ważne importy z biblioteki PDFBox
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
// IMPORTY dla obrazków
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
// --- NOWY, POPRAWNY IMPORT ---
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;


import javax.swing.*;
// IMPORTY dla obrazków
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
// IMPORT do wczytywania zasobów
import java.io.InputStream;

/**
 * Klasa serwisowa odpowiedzialna za generowanie raportów PDF
 * z wybranych wierszy przy użyciu Apache PDFBox.
 */
public class PdfExport {
    private PDType0Font FONT_BOLD;
    private PDType0Font FONT_REGULAR;

    //scieżka do ariala
    private final String FONT_PATH_REGULAR = "C:/Windows/Fonts/arial.ttf";
    private final String FONT_PATH_BOLD = "C:/Windows/Fonts/arialbd.ttf";


    public final String Load_Bearing_Pope = "jp2.jpg";

    private final int FONT_SIZE_TITLE = 18;
    private final int FONT_SIZE_HEADER = 12;
    private final int FONT_SIZE_BODY = 10;
    private final float ROW_HEIGHT = 20f;
    private final float CELL_MARGIN = 5f;


    public void exportRowToPdf(Object[] rowData, String[] columnNames, Component parentComponent) throws IOException {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz raport PDF");
        fileChooser.setSelectedFile(new File("Raport_Testu_ID_" + rowData[0] + ".pdf"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".pdf");
            }
            @Override
            public String getDescription() {
                return "Pliki PDF (*.pdf)";
            }
        });

        int userSelection = fileChooser.showSaveDialog(parentComponent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
        }

        //magia
        try (PDDocument document = new PDDocument()) {

            try {
                FONT_REGULAR = PDType0Font.load(document, new File(FONT_PATH_REGULAR));
                FONT_BOLD = PDType0Font.load(document, new File(FONT_PATH_BOLD));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parentComponent,
                        "KRYTYCZNY BŁĄD: Nie można wczytać czcionki Arial z C:/Windows/Fonts/\n" + e.getMessage(),
                        "Błąd Czcionki",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDRectangle mediaBox = page.getMediaBox();
            float margin = 50;
            float width = mediaBox.getWidth() - 2 * margin;
            float startY = mediaBox.getUpperRightY() - margin;

            //zapis treści
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.beginText();
                contentStream.setFont(FONT_BOLD, FONT_SIZE_TITLE);
                contentStream.newLineAtOffset(margin, startY);
                contentStream.showText("Raport Szczegółowy Testu Sprężyny");
                contentStream.endText();

                float currentY = startY - (FONT_SIZE_TITLE * 1.5f);

                //table
                float tableBottomY = drawDataTable(contentStream, width, margin, currentY, columnNames, rowData);

                drawImage(document, contentStream, mediaBox, margin);

            }

            document.save(fileToSave);

            JOptionPane.showMessageDialog(parentComponent,
                    "Plik PDF został pomyślie wyeksportowany do:\n" + fileToSave.getAbsolutePath(),
                    "Eksport Udany",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //do robienia tabeli
    //tu dużo z chata bo pozycjonowanie tekstu etc
    private float drawDataTable(PDPageContentStream cs, float pageWidth, float margin, float y, String[] headers, Object[] values) throws IOException {


        float tableTopY = y; // Góra tabeli
        float tableBottomY = y - (ROW_HEIGHT * 2); // Tabela ma 2 wiersze
        float headerRowY = y - ROW_HEIGHT;
        float valueRowY = y - (ROW_HEIGHT * 2);

        int colCount = headers.length;
        float cellWidth = pageWidth / colCount;

        cs.setNonStrokingColor(Color.LIGHT_GRAY);
        cs.addRect(margin, headerRowY, pageWidth, ROW_HEIGHT);
        cs.fill();
        cs.setNonStrokingColor(Color.WHITE);
        cs.addRect(margin, valueRowY, pageWidth, ROW_HEIGHT);
        cs.fill();


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

        cs.setFont(FONT_BOLD, FONT_SIZE_HEADER);
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

        cs.setFont(FONT_REGULAR, FONT_SIZE_BODY);
        textX = margin + CELL_MARGIN; // Reset pozycji X
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

    //najważniejsza funkcja w kodzie
    private void drawImage(PDDocument document, PDPageContentStream cs, PDRectangle mediaBox, float margin) throws IOException {

        PDImageXObject pdImage;
        try {
            InputStream is = getClass().getResourceAsStream(Load_Bearing_Pope);
            if (is == null) {
                System.err.println("BŁĄD: Nie można znaleźć obrazka: " + Load_Bearing_Pope);
                System.err.println("Upewnij się, że plik znajduje się w pakiecie 'main/java/me/mkkg/springstester/Database/'");
                return;
            }

            BufferedImage bimg = ImageIO.read(is);
            if (bimg == null) {
                throw new IOException("Nie można odczytać pliku obrazka. Upewnij się, że to poprawny plik .png lub .jpg.");
            }

            pdImage = LosslessFactory.createFromImage(document, bimg);
            is.close();

        } catch (Exception e) {
            System.err.println("Błąd podczas wczytywania statycznego obrazka: " + e.getMessage());
            e.printStackTrace();
            return;
        }



        float imageWidth = pdImage.getWidth();
        float imageHeight = pdImage.getHeight();
        float maxWidth = 200f; //Maksymalna szerokość

        float scale = 1.0f;
        if (imageWidth > maxWidth) {
            scale = maxWidth / imageWidth;
        }

        float scaledWidth = imageWidth * scale;
        float scaledHeight = imageHeight * scale;

        //Pozycja
        float x = margin + (mediaBox.getWidth() - 2 * margin - scaledWidth) / 2;
        float y = mediaBox.getLowerLeftY() + margin;

        cs.drawImage(pdImage, x, y, scaledWidth, scaledHeight);
    }
}