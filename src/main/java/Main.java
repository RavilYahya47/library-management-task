package java;

import java.dao.AuthorDAO;
import java.dao.impl.AuthorDAOImpl;
import java.dao.impl.BookDAOImpl;
import java.model.Book;
import java.service.LibraryService;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        BookDAOImpl bookDAO = new BookDAOImpl();
        AuthorDAO authorDAO = new AuthorDAOImpl();
        LibraryService libraryService = new LibraryService(authorDAO, bookDAO);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int selection =fromScanner();
            switch (selection) {
                case 0: {
                    System.out.println("Exit to system...");
                    return;
                }
                case 1: {
                    System.out.print("All books: " );
                    List<Book> all = bookDAO.findAll();
                    for (Book book : all) {
                        System.out.println(book + ", ");
                    }
                    break;
                }
                case 2: {
                    System.out.print("Enter Author name: ");
                    String answer = scanner.nextLine();
                    for (Book book : libraryService.findBooksByAuthor(answer)) {
                        System.out.println(book);
                    }
                    break;
                }
                case 3: {
                    List<Book> availableBooks = libraryService.findAvailableBooks();
                    for (Book book: availableBooks) {
                        System.out.print(book + ", ");
                    }
                }
                case 4:
                    System.out.print("choose you book id: ");
                    int answer = scanner.nextInt();
                    libraryService.borrowBook(answer);
                    break;
                case 5:
                    System.out.print("Enter your book id which you like: ");
                    int bookName = scanner.nextInt();
                    libraryService.returnBook(bookName);
                    break;
                case 6:{
                    Map<String, Long> bookStatisticsByGenre = libraryService.getBookStatisticsByGenre();
                    for (Map.Entry<String, Long> entry : bookStatisticsByGenre.entrySet()) {
                        System.out.println("Genre : " + entry.getKey() + " ------ " + "Total: " + entry.getValue());
                    }
                    break;
                }
                default:
                    System.out.println("Your selection is not correct");
                    break;
            }
        }
    }

    public static int fromScanner() {
        Scanner scanner = new Scanner(System.in);
        String string = "=== Kitab İdarəetmə Sistemi ===\n" +
                "        1. Bütün kitabları göstər\n" +
                "        2. Müəllifə görə kitab axtar\n" +
                "        3. Mövcud kitabları göstər\n" +
                "        4. Kitab icarəyə ver\n" +
                "        5. Kitabı qaytart\n" +
                "        6. Janr üzrə statistika\n" +
                "        0. Çıxış\n" +
                "\nSeciminizi daxil edin : ";

        return scanner.nextInt();
    }
}
