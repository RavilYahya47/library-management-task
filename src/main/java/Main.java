package main.java;

import main.java.dao.impl.AuthorDaoImpl;
import main.java.dao.impl.BookDaoImpl;
import main.java.model.Book;
import main.java.service.LibraryService;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LibraryService service = new LibraryService(new AuthorDaoImpl(), new BookDaoImpl());

        while (true) {
            System.out.println("=== Kitab İdarəetmə Sistemi ===");
            System.out.println("1. Bütün kitabları göstər");
            System.out.println("2. Müəllifə görə kitab axtar");
            System.out.println("3. Mövcud kitabları göstər");
            System.out.println("4. Kitab icarəyə ver");
            System.out.println("5. Kitabı qaytar");
            System.out.println("6. Janr üzrə statistika");
            System.out.println("0. Çıxış");
            System.out.print("Seçiminizi daxil edin: ");
            int secim = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (secim) {
                    case 1:
                        service.findAll().forEach(System.out::println);
                        break;
                    case 2:
                        System.out.print("Muellif adi: ");
                        String name = scanner.nextLine();
                        service.findBooksByAuthor(name).forEach(System.out::println);
                        break;
                    case 3:
                        service.findAvailableBooks().forEach(System.out::println);
                        break;
                    case 4:
                        System.out.print("Kitab ID: ");
                        int id1 = scanner.nextInt();
                        service.borrowBook(id1);
                        System.out.println("Kitab icareye verildi.");
                        break;
                    case 5:
                        System.out.print("Kitab ID: ");
                        int id2 = scanner.nextInt();
                        service.returnBook(id2);
                        System.out.println("Kitab qaytarildi.");
                        break;
                    case 6:
                        Map<String, Long> stats = service.getBookStatisticsByGenre();
                        stats.forEach((genre, count) -> System.out.println(genre + ": " + count));
                        break;
                    case 7:
                        Optional<Book> longestBook = service.findLongestBook();

                        if (longestBook.isPresent()) {
                            System.out.println("Ən qalın kitab: " + longestBook.get());
                        } else {
                            System.out.println("Kitab tapılmadı.");
                        }
                        break;
                    case 8:
                        System.out.print("İl daxil edin: ");
                        int year = scanner.nextInt();
                        service.findBooksPublishedAfter(year).forEach(System.out::println);
                        break;
                    case 0:
                        System.exit(0);
                    default:
                        System.out.println("Yanlis secim!");
                }
            } catch (Exception e) {
                System.out.println("Xeta: " + e.getMessage());
            }
        }
    }
}
