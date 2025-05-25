package main.java.service;

import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LibraryService {

    List<Book> findBooksByAuthor(String authorName) throws SQLException;

    List<Book> findAvailableBooks() throws SQLException;

    void borrowBook(int bookId) throws SQLException;

    void returnBook(int bookId) throws SQLException;

    Map<String, Long> getBookStatisticsByGenre() throws SQLException;

}