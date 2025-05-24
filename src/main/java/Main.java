package main.java;

import main.java.dao.AuthorDAOImpl;
import main.java.dao.DatabaseConnection;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            var con = DatabaseConnection.getConnection();
            System.out.println(new AuthorDAOImpl(con).findAll());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        showMenu();
    }

    private static void showMenu(){
        System.out.println("=== Kitab İdarəetmə Sistemi ===");
        System.out.println("1. Bütün kitabları göstər");
        System.out.println("2. Müəllifə görə kitab axtar");
        System.out.println("3. Mövcud kitabları göstər");
        System.out.println("4. Kitab icarəyə ver");
        System.out.println("5. Kitabı qaytar");
        System.out.println("6. Janr üzrə statistika");
        System.out.println("0. Çıxış");

    }

    private static void prompt(){
        System.out.println("Seçiminizi daxil edin:");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
    }
}