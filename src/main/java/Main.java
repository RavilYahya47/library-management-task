package main.java;

import main.java.dao.AuthorDAOImpl;
import main.java.dao.BookDAOImpl;
import main.java.model.Book;
import main.java.service.LibraryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    public static Connection connection = DatabaseConnection.getConnection();

    public static AuthorDAOImpl authorDAO = new AuthorDAOImpl(connection);

    public static BookDAOImpl bookDAO = new BookDAOImpl(connection);

    public static LibraryService libraryService = new LibraryService(authorDAO, bookDAO, connection);

    public static void main(String[] args) {

        while (true) {
            menu();
            System.out.print("Enter your choice: ");
            int choice = getInt();
            getUserChoice(choice);
        }
    }

    public static void menu() {
        System.out.println("\n=== Library Management System ===\n");
        System.out.println("1. Show all books");
        System.out.println("2. Search book by author");
        System.out.println("3. Show available books");
        System.out.println("4. Borrow a book");
        System.out.println("5. Return the book");
        System.out.println("6. Book statistic by genre");
        System.out.println("0. Exit");
    }

    public static void getUserChoice(int choice) {
        try {
            switch (choice) {
                case 1:
                    List<Book> books = bookDAO.findAll();
                    bookDAO.allBooksWithTable(books);
                    break;
                case 2:
                    System.out.print("Enter author's name: ");
                    String authorName = sc.nextLine();
                    List<Book> booksByAuthor = libraryService.findBooksByAuthor(authorName);
                    if (booksByAuthor != null) {
                        bookDAO.allBooksWithTable(booksByAuthor);
                    }
                    break;
                case 3:
                    Predicate<Book> condition = Book::getIsAvailable;
                    List<Book> availableBooks = libraryService.findAvailableBooks(condition);
                    bookDAO.allBooksWithTable(availableBooks);
                    break;
                case 4:
                    System.out.print("Enter book ID: ");
                    int bookId1 = getInt();
                    libraryService.borrowBook(bookId1);
                    break;
                case 5:
                    System.out.print("Enter book ID: ");
                    int bookId2 = getInt();
                    libraryService.returnBook(bookId2);
                    break;
                case 6:
                    Map<String, Long> map = libraryService.getBookStatisticsByGenre();
                    System.out.printf("%-20s %-5s%n", "Genre", "Count");
                    System.out.println("--------------------------");
                    map.forEach((genre, count) -> System.out.printf("%-20s %5d%n", genre, count));
                    break;
                case 0:
                    System.out.println("Program is finished...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        } catch (RuntimeException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static int getInt() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
