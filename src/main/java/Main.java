package main.java;

import main.java.dao.AuthorDAOImpl;
import main.java.dao.BookDAOImpl;
import main.java.dao.DatabaseConnection;
import main.java.model.Book;
import main.java.service.AuthorService;
import main.java.service.BookService;

import java.io.IOException;
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

            while (true){
                showMenu();
                Scanner sc = new Scanner(System.in);
                int command = sc.nextInt();
                String arg = sc.nextLine();

                dispatch(command, arg);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("=== Kitab İdarəetmə Sistemi ===");
        System.out.println("1. Bütün kitabları göstər");
        System.out.println("2. Müəllifə görə kitab axtar");
        System.out.println("3. Mövcud kitabları göstər");
        System.out.println("4. Kitab icarəyə götür");
        System.out.println("5. Kitabı qaytar");
        System.out.println("6. Janr üzrə statistika");
        System.out.println("7. Müəllifləri göstər");
        System.out.println("8. Müəllifləri adına görə göstər");
        System.out.println("9. Müəllifləri millətinə görə göstər");
        System.out.println("0. Çıxış");
    }

    private static void dispatch(int command, String arg) throws SQLException {
        switch (command) {
            case 1 -> books.filterBy(_ -> true);
            case 2 -> books.filterBy(book -> book.getTitle().toLowerCase().contains(arg.trim().toLowerCase()));
            case 3 -> books.filterBy(Book::isAvailable);
            case 4 -> books.hireBook();
            case 5 -> books.returnBook();
            case 6 -> books.filterBy(book -> book.getGenre().toLowerCase().contains(arg.trim().toLowerCase()));
            case 7 -> authors.filterBy(_ -> true);
            case 8 -> authors.filterBy(author -> author.getName().toLowerCase().contains(arg.trim().toLowerCase()));
            case 9 -> authors.filterBy(author -> author.getNationality().toLowerCase().contains(arg.trim().toLowerCase()));
            case 0 -> System.exit(0);
        }
    }


}