package main.java.service;

import main.java.dao.BookDAO;
import main.java.model.Book;

import java.util.ArrayList;
import java.util.List;

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

    public List<Book> getBooks() {
        return new ArrayList<Book>();
    }

    public List<Book> getBooksByYear(int year) {
        return new ArrayList<Book>();
    }

    public List<Book> getBooksByAuthorName(String authorName) {
        return new ArrayList<Book>();
    }

    public List<Book> getBooksByTitle(String title) {
        return new ArrayList<Book>();
    }

    public List<Book> getBooksByGenre(String genre) {
        return new ArrayList<Book>();
    }
}
