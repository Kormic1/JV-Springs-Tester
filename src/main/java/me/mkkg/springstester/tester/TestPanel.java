package main.java.me.mkkg.springstester.tester;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TestPanel {

    private static TestPanel instance;


    private JTable dataTable;
    private DefaultTableModel tableModel;

    public JPanel createPanel() {
        // TestPanel inst = new TestPanel();
        TestPanel inst = this; //to samo co w MenuPanel

        JPanel panel = new JPanel(new BorderLayout(10, 10)); // Zmiana layoutu
        panel.setBackground(new Color(200, 200, 200));

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton writeButton = new JButton("Zapisz nowy test (dane demo)");
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

        JScrollPane scrollPane = new JScrollPane(dataTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Usuń wybrany wiersz");
        deleteButton.setFocusable(false);


        deleteButton.addActionListener(e -> handleDeleteSelectedRow(panel));
        actionPanel.add(deleteButton);
        panel.add(actionPanel, BorderLayout.SOUTH);


        refreshTableData();

        TestPanel.instance = inst;
        return panel;
    }

    private void handleWriteToDatabase(Component parentComponent) {
        try {
            String dane1 = "Wpis " + System.currentTimeMillis();
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

    public static TestPanel getInstance() {
        return instance;
    }
}

