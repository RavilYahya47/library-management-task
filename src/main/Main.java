package main;

import main.java.dao.DatabaseConnection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.getConnection();

    }
}
