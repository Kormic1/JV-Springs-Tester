package main.java.me.mkkg.springstester.tester.cards;

import main.java.me.mkkg.springstester.database.DatabaseService;
import main.java.me.mkkg.springstester.database.ImageBase64Handler;
import main.java.me.mkkg.springstester.pdf.PDFUtils;
import main.java.me.mkkg.springstester.pdf.PDFHandler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class ResultsCard extends Card {

    private static final Color BG_COLOR = new Color(200, 200, 200);

    private JTable dataTable;
    private DefaultTableModel tableModel;
    private final PDFHandler PDFHandler;
    private static ResultsCard instance;

    private JTextField searchField;
    private TableRowSorter<TableModel> sorter;

    public ResultsCard() {

        ResultsCard.instance = this;
        this.PDFHandler = new PDFHandler();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topPanel.setOpaque(false);

        JButton writeButton = new JButton("Zapisz nowy test");
        writeButton.setFocusable(false);
        writeButton.addActionListener(e -> handleWriteToDatabase(panel));
        topPanel.add(writeButton);

        JLabel searchLabel = new JLabel("Szukaj:");
        searchField = new JTextField(20); // Szerokość pola

        topPanel.add(searchLabel);
        topPanel.add(searchField);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Data 1", "Data 2", "Data 3"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Komórki nieedytowalne
            }
        };

        dataTable = new JTable(tableModel);


        sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Usuń wybrany test");
        deleteButton.setFocusable(false);
        deleteButton.addActionListener(e -> handleDeleteSelectedRow(panel));
        actionPanel.add(deleteButton);

        JButton exportPdfButton = new JButton("Eksportuj wybrany test do PDF");
        exportPdfButton.setFocusable(false);
        exportPdfButton.addActionListener(e -> handleExportToPdf(panel));
        actionPanel.add(exportPdfButton);

        panel.add(actionPanel, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }
        });

        refreshTableData();
    }

    private void newFilter() {
        String text = searchField.getText();
        
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
            return;
        }

        try {
            String[] parts = text.trim().split("\\s+");

            List<RowFilter<TableModel, Object>> filters = new ArrayList<>();

            for (String part : parts) {
                if (!part.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + part));
                }
            }
            
            sorter.setRowFilter(RowFilter.andFilter(filters));

        } catch (java.util.regex.PatternSyntaxException e) {
            
        }
    }

    private void handleExportToPdf(Component parentComponent) {
        int selectedRowInView = dataTable.getSelectedRow();

        if (selectedRowInView == -1) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Proszę najpierw zaznaczyć wiersz do eksportu.",
                    "Brak zaznaczenia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = dataTable.convertRowIndexToModel(selectedRowInView);

        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < rowData.length; i++) {
            rowData[i] = tableModel.getValueAt(modelRow, i);
        }

        String[] columnNames = new String[tableModel.getColumnCount()];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }

        try {
            PDFHandler.exportRowToPdf(rowData, columnNames, parentComponent);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Błąd Wejścia/Wyjścia (IOException) podczas eksportu PDF:\n" + ex.getMessage(),
                    "Błąd Zapisu Pliku",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Wystąpił nieoczekiwany błąd podczas eksportu PDF:\n" + ex.getMessage(),
                    "Błąd Ogólny",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleWriteToDatabase(Component parentComponent) {
        try {
            String dane1 = PDFUtils.LOAD_BEARING_POPE + System.currentTimeMillis();
            String dane2 = "Jakiś test";
            String dane3 = "Wynik: OK";

            String b64Image = ImageBase64Handler.encode(TestingCard.TestIm);

            DatabaseService.getInstance().writeTestData(dane1, dane2, dane3, b64Image);
            refreshTableData();

            JOptionPane.showMessageDialog(parentComponent,
                    "Pomyślnie zapisano do bazy.",
                    "Sukces",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Błąd Bazy Danych (SQLException) podczas zapisu:\n" + ex.getMessage(),
                    "Błąd SQL",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleDeleteSelectedRow(Component parentComponent) {
        int selectedRowInView = dataTable.getSelectedRow();

        if (selectedRowInView == -1) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Proszę najpierw zaznaczyć wiersz do usunięcia.",
                    "Brak zaznaczenia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = dataTable.convertRowIndexToModel(selectedRowInView);
        int testId = (Integer) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(parentComponent,
                "Czy na pewno chcesz usunąć wiersz o ID: " + testId + "?",
                "Potwierdź usunięcie",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DatabaseService.getInstance().deleteTestData(testId);
                refreshTableData();

                JOptionPane.showMessageDialog(parentComponent,
                        "Pomyślnie usunięto wiersz.",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(parentComponent,
                        "Błąd Bazy Danych (SQLException) podczas usuwania:\n" + ex.getMessage(),
                        "Błąd SQL",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public void refreshTableData() {
        try {
            List<Object[]> data = DatabaseService.getInstance().readAllTestData();

            if (tableModel != null) {
                tableModel.setRowCount(0);
            }

            for (Object[] row : data) {
                tableModel.addRow(row);
            }


            if (!searchField.getText().isEmpty()) {
                newFilter();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(dataTable,
                    "Błąd podczas odświeżania danych:\n" + ex.getMessage(),
                    "Błąd Bazy Danych",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static ResultsCard getInstance() {
        return instance;
    }
}
