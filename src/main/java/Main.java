package main.java;

import main.java.dao.DatabaseConnection;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        try {
            var con = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}