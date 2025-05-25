package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException{
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (SQLException e) {
            throw new SQLException("Database connection error!");
        }

    }
}
