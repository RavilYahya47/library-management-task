package main.java.service;

import main.java.dao.BookDAO;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

public class BookService {
    private static BookService service;
    private final BookDAO dao;

    public static BookService of(BookDAO dao) {
        if (service == null) {
            service = new BookService(dao);
        }

        return service;
    }

    private BookService(BookDAO dao) {
        this.dao = dao;
    }

    public void filterBy(Predicate<Book> filter) throws SQLException {
        List<Book> books = dao.findAll().stream().filter(filter).toList();

        if (!books.isEmpty()) {
            books.forEach(System.out::println);
        }
        else {
            System.out.println("No authors found");
        }
    }

    public void hireBook() throws SQLException {
        System.out.print("Kitabın id-sini daxil et: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        Optional<Book> book = dao.findById(id);
        if (book.isPresent()) {
            Book book1 = book.get();
            book1.setAvailable(false);
            dao.update(book1);
        }
        else {
            System.out.println("Book not found");
        }
    }

    public void returnBook() throws SQLException {
        System.out.print("Kitabın id-sini daxil et: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        Optional<Book> book = dao.findById(id);
        if (book.isPresent()) {
            Book book1 = book.get();
            book1.setAvailable(true);
            dao.update(book1);
        }
        else {
            System.out.println("Book not found");
        }
    }
}
