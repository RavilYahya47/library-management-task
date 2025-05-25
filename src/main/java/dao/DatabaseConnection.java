package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() throws SQLException {

        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/library_db",
                    "postgres",
                    "12345"
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return getConnection();
    }

}
