package library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        // Connection qaytaran method yazın
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        // Exception handling əlavə edin
    }
}
