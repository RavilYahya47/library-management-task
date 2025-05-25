package main.java;

import main.java.DAO.impl.AuthorDAOImpl;
import main.java.DAO.impl.BookDAOImpl;
import main.java.exception.BookNotAvailableException;
import main.java.exception.BookNotFoundException;
import main.java.model.Book;
import main.java.service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService(new AuthorDAOImpl(), new BookDAOImpl());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-----");
            System.out.println("== Kitab İdarəetmə Sistemi ==");
            System.out.println("-----");
            System.out.println("1. Bütün kitabları göstər");
            System.out.println("2. Müəllifə görə kitab axtar");
            System.out.println("3. Mövcud kitabları göstər");
            System.out.println("4. Kitab icarəyə ver");
            System.out.println("5. Kitabı qaytart");
            System.out.println("6. Janr üzrə statistika");
            System.out.println("7. Janr üzrə qruplaşdırılmış kitablar");
            System.out.println("8. Ən çox səhifəli kitab");
            System.out.println("9. Müəyyən ildən sonrakı kitablar");
            System.out.println("10. Janr üzrə kitabları göstər");
            System.out.println("0. Çıxış");
            System.out.println("-----");
            System.out.print("Seçiminizi daxil edin: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                        List<Book> allBooks = service.findBooksBy(b -> true);
                        allBooks.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Müəllifin adını daxil edin: ");
                        String name = scanner.nextLine();
                        List<Book> books = service.findBooksByAuthor(name);
                        books.forEach(System.out::println);
                    }
                    case 3 -> {
                        List<Book> availableBooks = service.findAvailableBooks();
                        availableBooks.forEach(System.out::println);
                    }
                    case 4 -> {
                        System.out.print("İcarəyə veriləcək kitabı daxil edin: (ID)");
                        int bookId = scanner.nextInt();
                        service.borrowBook(bookId);
                        System.out.println("Kitab icarəyə verildi.");
                    }
                    case 5 -> {
                        System.out.print("Qaytarılacaq kitabı daxil edin: (ID)");
                        int bookId = scanner.nextInt();
                        service.returnBook(bookId);
                        System.out.println("Kitab qaytarıldı");
                    }
                    case 6 -> {
                        Map<String, Long> stats = service.getBookStatisticsByGenre();
                        stats.forEach((genre, count) ->
                                System.out.println("Janr: " + genre + " - Say: " + count));
                    }
                    case 7 -> {
                        Map<String, List<Book>> grouped = service.groupBooksByGenre();
                        grouped.forEach((genre, books) -> {
                            System.out.println("Janr: " + genre);
                            books.forEach(System.out::println);
                        });
                    }
                    case 8 -> {
                        Optional<Book> maxPageBook = service.findBookWithMostPages();
                        maxPageBook.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("Kitab tapılmadı")
                        );
                    }
                    case 9 -> {
                        System.out.print("İli daxil edin: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        List<Book> recentBooks = service.filterBooksAfterYear(year);
                        recentBooks.forEach(System.out::println);
                    }
                    case 10 -> {
                        System.out.print("Janr daxil edin: ");
                        String genre = scanner.nextLine();

                        List<Book> booksByGenre = service.findBooksByGenre(genre);
                        if (booksByGenre.isEmpty()) {
                            System.out.println("Bu janr üzrə kitab tapılmadı");
                        } else {
                            booksByGenre.forEach(System.out::println);
                        }
                    }
                    case 0 -> {
                        System.out.println("Sistemdən çıxdınız");
                        return;
                    }
                    default -> System.out.println("Yanlış seçim");
                }
            } catch (SQLException | BookNotFoundException | BookNotAvailableException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
