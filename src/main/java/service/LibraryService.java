package main.java.service;

import main.java.dao.AuthorDAO;
import main.java.dao.BookDAO;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;


    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        return null;
    }

    public List<Book> findAvailableBooks() throws SQLException {
        return null;
    }

    public void borrowBook(int bookId) throws SQLException {

    }

    public void returnBook(int bookId) throws SQLException {

    }

    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        return null;
    }
}