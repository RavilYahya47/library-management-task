package main.java;

import main.java.dao.AuthorDAO;
import main.java.dao.AuthorDAOImpl;
import main.java.dao.BookDAOImpl;
import main.java.dao.DatabaseConnection;
import main.java.service.AuthorService;
import main.java.service.BookService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static AuthorService authors;
    private static BookService books;

    public static void main(String[] args) {

        try {
            Connection con = DatabaseConnection.getConnection();
            authors = AuthorService.of(AuthorDAOImpl.of(con));
            books = BookService.of(BookDAOImpl.of(con));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        showMenu();
    }

    private static void showMenu() {
        System.out.println("=== Kitab İdarəetmə Sistemi ===");
        System.out.println("1. Bütün kitabları göstər");
        System.out.println("2. Müəllifə görə kitab axtar");
        System.out.println("3. Mövcud kitabları göstər");
        System.out.println("4. Kitab icarəyə ver");
        System.out.println("5. Kitabı qaytar");
        System.out.println("6. Janr üzrə statistika");
        System.out.println("7. Müəllifləri göstər");
        System.out.println("0. Çıxış");
    }

    private static void prompt() {
        System.out.println("Seçiminizi daxil edin:");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
    }

    private static void dispatch(int command) {
        switch (command) {
            case 1 -> Commands.filterBooksByYear();
        }
    }


}