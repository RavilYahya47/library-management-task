package main.java.service;

import main.java.dao.BookDAO;
import main.java.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class BookService {
    private static BookService service;
    private BookDAO dao;

    public static BookService of(BookDAO dao) {
        if (service == null) {
            service = new BookService(dao);
        }

        return service;
    }

    private BookService(BookDAO dao) {
        this.dao = dao;
    }

    public void filterBy(Predicate<Book> filter) {

    };

    public void hireBook() {}

    public void returnBook() {}
}
