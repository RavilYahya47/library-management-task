package main.java.dao.impl;

import main.java.dao.BookDAO;
import main.java.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Book save(Book book) throws SQLException {
        return null;
    }

    @Override
    public void update(Book book) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {

    }
}
