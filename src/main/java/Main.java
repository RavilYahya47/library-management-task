package main.java;

import main.java.DAO.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();
        System.out.println(connection);

    }
}
