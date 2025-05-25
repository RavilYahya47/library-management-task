package main;

import main.java.dao.impl.AuthorDAOImpl;
import main.java.dao.impl.BookDAOImpl;
import main.java.service.LibraryService;
import main.java.service.impl.LibraryServiceImpl;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        LibraryService libraryService = new LibraryServiceImpl(
                new AuthorDAOImpl(),
                new BookDAOImpl()
        );

        int choice;

        do {
            System.out.println("\n=== Kitab İdarəetmə Sistemi ===");
            System.out.println("1. Bütün kitabları göstər");
            System.out.println("2. Müəllifə görə kitab axtar");
            System.out.println("3. Mövcud kitabları göstər");
            System.out.println("4. Kitabı icarəyə ver");
            System.out.println("5. Kitabı qaytar");
            System.out.println("6. Janr üzrə statistika");
            System.out.println("0. Çıxış");
            System.out.print("Seçiminizi daxil edin: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1, 3 -> {
                        List<Book> allBooks = libraryService.findAvailableBooks();
                        allBooks.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Müəllifin adını daxil edin: ");
                        String authorName = scanner.nextLine();
                        List<Book> books = libraryService.findBooksByAuthor(authorName);
                        books.forEach(System.out::println);
                    }
                    case 4 -> {
                        System.out.print("İcarəyə veriləcək kitabın ID-sini daxil edin: ");
                        int bookId = scanner.nextInt();
                        libraryService.borrowBook(bookId);
                        System.out.println("Kitab icarəyə verildi.");
                    }
                    case 5 -> {
                        System.out.print("Qaytarılacaq kitabın ID-sini daxil edin: ");
                        int bookId = scanner.nextInt();
                        libraryService.returnBook(bookId);
                        System.out.println("Kitab qaytarıldı.");
                    }
                    case 6 -> {
                        Map<String, Long> stats = libraryService.getBookStatisticsByGenre();
                        stats.forEach((genre, count) -> System.out.println(genre + ": " + count));
                    }
                    case 0 -> System.out.println("Çıxılır...");
                    default -> System.out.println("Yanlış seçim! Yenidən cəhd edin.");
                }
            } catch (SQLException e) {
                System.out.println("Xəta baş verdi: " + e.getMessage());
            }

        } while (choice != 0);

        scanner.close();

    }
}