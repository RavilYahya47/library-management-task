package main.java.DAO;

import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    List<Book> findAll() throws SQLException;

    Optional<Book> findById(int id) throws SQLException;

    Book save(Book book) throws SQLException;

    void update(Book book) throws SQLException;

    void deleteById(int id) throws SQLException;

    List<Book> findByAuthorName(String authorName) throws SQLException;

    List<Book> findAvailableBooks() throws SQLException;

    List<Book> findByGenre(String genre) throws SQLException;
}