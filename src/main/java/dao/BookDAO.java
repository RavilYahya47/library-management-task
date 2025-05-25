package main.java.dao;

import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDAO {
    List<Book> findAll() throws SQLException;
    Optional<Book> findById(int id) throws SQLException;
    Book save(Book author) throws SQLException;
    void update(Book author) throws SQLException;
    void deleteById(int id) throws SQLException;
}
