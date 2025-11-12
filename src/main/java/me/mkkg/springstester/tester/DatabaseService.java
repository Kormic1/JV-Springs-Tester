package main.java.me.mkkg.springstester.tester;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DatabaseService {

    private static DatabaseService instance;

    //jest pewnie bardziej kompaktowy zapis ale tak jest czytelniej

    private final String dbHost = "localhost";
    private final String dbPort = "5432";
    private final String dbName = "test";
    private final String dbUser = "postgres";
    private final String dbPassword = "postgres"; // Twoje hasło
    private final String dbSslMode = "prefer";
    private final String dbConnectTimeout = "10";

    private final String dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
    private final Properties dbProps;


    private DatabaseService() {
        dbProps = new Properties();
        dbProps.setProperty("user", dbUser);
        dbProps.setProperty("password", dbPassword);
        dbProps.setProperty("sslmode", dbSslMode);
        dbProps.setProperty("connect_timeout", dbConnectTimeout);

        try {
            //sterownik
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" Nie znaleziono sterownika PostgreSQL JDBC");
            e.printStackTrace();
        }
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    //write, zmienne placeholdery
    public void writeTestData(String data1, String data2, String data3) throws SQLException {
        String sql = "INSERT INTO test_table(test_data1, test_data2, test_data3) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbProps);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, data1);
            pstmt.setString(2, data2);
            pstmt.setString(3, data3);
            pstmt.executeUpdate();
        }
    }
    //zczytywanie/tabelkowanie
    public List<Object[]> readAllTestData() throws SQLException {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT test_id, test_data1, test_data2, test_data3 FROM test_table ORDER BY test_id ASC";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbProps);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(new Object[]{
                        rs.getInt("test_id"),
                        rs.getString("test_data1"),
                        rs.getString("test_data2"),
                        rs.getString("test_data3")
                });
            }
        }
        return results;
    }
    //delet
    public void deleteTestData(int testId) throws SQLException {
        String sql = "DELETE FROM test_table WHERE test_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbProps);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, testId);
            pstmt.executeUpdate();
        }
    }
}

