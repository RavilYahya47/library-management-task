package main.java.dao;

import main.java.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuthorDAO {

    List<Author> findAll() throws SQLException;
    Optional<Author> findById(int id) throws SQLException;
    Author save(Author author) throws SQLException;
    void update(Author author) throws SQLException;
    void deleteById(int id) throws SQLException;

}

