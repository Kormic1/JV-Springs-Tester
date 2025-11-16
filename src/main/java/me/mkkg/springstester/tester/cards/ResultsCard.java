package main.java.me.mkkg.springstester.tester.cards;

import main.java.me.mkkg.springstester.Database.DatabaseService;
import main.java.me.mkkg.springstester.Database.PdfExport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.io.IOException;

public class ResultsCard extends Card {

    private static final Color BG_COLOR = new Color(200, 200, 200);

    private JTable dataTable;
    private DefaultTableModel tableModel;
    private final PdfExport PdfExport;
    private static ResultsCard instance;

    public ResultsCard() {

        ResultsCard.instance = this;
        this.PdfExport = new PdfExport();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton writeButton = new JButton("Zapisz nowy test"); //docelowo ten przycisk nie istnieje
                                                                    //jego funkcję będzie pełniło wykonanie testu
                                                                    //zakładając, że test będzie zajmował ileś czasu
                                                                    //można zrobić wait statyczny po przyciśnięciu
                                                                    //startu testu albo jakiś scheduler
                                                                    //jakby nas ambicja naszła
        writeButton.setFocusable(false);

        writeButton.addActionListener(e -> handleWriteToDatabase(panel));
        addPanel.add(writeButton);
        panel.add(addPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Data 1", "Data 2", "Data 3"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Komórki nieedytowalne
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //tabela
        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Usuń wybrany test");
        deleteButton.setFocusable(false);

        deleteButton.addActionListener(e -> handleDeleteSelectedRow(panel));
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);


        JButton exportPdfButton = new JButton("Eksportuj wybrany test do PDF");
        exportPdfButton.setFocusable(false);
        exportPdfButton.addActionListener(e -> handleExportToPdf(panel));

        actionPanel.add(exportPdfButton);
        panel.add(actionPanel, BorderLayout.SOUTH);
        refreshTableData();
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

        //Nie ma sensu zapisywać do tabeli danych, do bazy natomiast jak najbardziej (chyba)
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < rowData.length; i++) {
            rowData[i] = tableModel.getValueAt(modelRow, i);
        }

        String[] columnNames = new String[tableModel.getColumnCount()];
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }

        try {
            PdfExport.exportRowToPdf(rowData, columnNames, parentComponent);

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
            String dane1 = PdfExport.Load_Bearing_Pope + System.currentTimeMillis();
            String dane2 = "Jakiś test";
            String dane3 = "Wynik: OK";

            DatabaseService.getInstance().writeTestData(dane1, dane2, dane3);
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

    //odswizanie
    public void refreshTableData() {
        try {
            List<Object[]> data = DatabaseService.getInstance().readAllTestData();

            if (tableModel != null) {
                tableModel.setRowCount(0);
            }

            for (Object[] row : data) {
                tableModel.addRow(row);
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