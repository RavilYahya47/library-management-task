package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/orient";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.jdbc.PgConnection");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }

        return connection;
    }
}
