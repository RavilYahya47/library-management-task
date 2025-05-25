package main.java.service;

import main.java.dao.BookDAO;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
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
    };

    public void hireBook() {}

    public void returnBook() {}
}
