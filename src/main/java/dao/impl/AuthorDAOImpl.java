package main.java.dao.impl;

import main.java.dao.AuthorDAO;
import main.java.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {
    @Override
    public List<Author> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Author save(Author author) throws SQLException {
        return null;
    }

    @Override
    public void update(Author author) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {

    }
}
