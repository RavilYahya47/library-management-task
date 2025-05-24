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
        ResultSet resultSet;
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM author WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            Optional<Author> optionalAuthor = null;
            while (resultSet.next()) {
                int ID = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int birth_year = resultSet.getInt(3);
                String nationality = resultSet.getString(4);
                Author author = new Author(ID, name, birth_year, nationality);
                optionalAuthor = Optional.of(author);
            }
            return optionalAuthor;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
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
