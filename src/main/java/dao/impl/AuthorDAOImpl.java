package java.dao.impl;

import java.dao.AuthorDAO;
import java.dao.DatabaseConnection;
import java.model.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM author");
        ResultSet resultSet = preparedStatement.executeQuery();
        return authors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM author WHERE id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

    }

    @Override
    public Author save(Author author) throws SQLException {

    }

    @Override
    public void update(Author author) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {

    }
}
