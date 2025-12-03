package main.java.me.mkkg.springstester.pdf;

import main.java.me.mkkg.springstester.database.DatabaseService;
import main.java.me.mkkg.springstester.database.ImageBase64Handler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFHandler {

    private PDType0Font fontBold;
    private PDType0Font fontRegular;

    private final String FONT_PATH_REGULAR = "C:/Windows/Fonts/arial.ttf";
    private final String FONT_PATH_BOLD = "C:/Windows/Fonts/arialbd.ttf";

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

        try (PDDocument document = new PDDocument()) {

            try {
                fontRegular = PDType0Font.load(document, new File(FONT_PATH_REGULAR));
                fontBold = PDType0Font.load(document, new File(FONT_PATH_BOLD));
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

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                contentStream.beginText();
                contentStream.setFont(fontBold, PDFUtils.FONT_SIZE_TITLE);
                contentStream.newLineAtOffset(margin, startY);
                contentStream.showText("Raport Szczegółowy Testu Sprężyny");
                contentStream.endText();

                float currentY = startY - (PDFUtils.FONT_SIZE_TITLE * 1.5f);

                float tableBottomY = PDFUtils.drawDataTable(
                        contentStream,
                        width,
                        margin,
                        currentY,
                        columnNames,
                        rowData,
                        fontBold,
                        fontRegular
                );

                float staticImageHeight = PDFUtils.drawStaticImage(document, contentStream, mediaBox, margin);

                try {
                    int testId = Integer.parseInt(rowData[0].toString());

                    String b64Image = DatabaseService.getInstance().getImageData(testId);

                    if (b64Image != null && !b64Image.isEmpty()) {
                        Image decodedImage = ImageBase64Handler.decode(b64Image);

                        if (decodedImage instanceof BufferedImage) {
                            BufferedImage bimg = (BufferedImage) decodedImage;

                            float padding = 20f;
                            float dynamicImageY = mediaBox.getLowerLeftY() + margin + staticImageHeight + padding;

                            PDFUtils.drawDynamicImage(document, contentStream, mediaBox, margin, bimg, dynamicImageY);
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Nie udało się pobrać/wygenerować obrazka z bazy: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            document.save(fileToSave);

            JOptionPane.showMessageDialog(parentComponent,
                    "Plik PDF został pomyślnie wyeksportowany do:\n" + fileToSave.getAbsolutePath(),
                    "Eksport Udany",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}