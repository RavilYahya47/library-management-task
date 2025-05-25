package main.java.dao.impl;

import main.java.dao.AuthorDAO;
import main.java.dao.DatabaseConnection;
import main.java.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    //PreparedStatement istifadə edin
    //ResultSet-dən Author obyektləri yaradın
    //Exception handling əlavə edin
    //Connection-ları düzgün bağlayın


    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                authors.add(mapResultSetToAuthor(resultSet));
            }
        }
        return authors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        String sql = "SELECT * FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToAuthor(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Author save(Author author) throws SQLException {
        String sql = "INSERT INTO authors (name, birthYear, nationality) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getBirthYear());
            preparedStatement.setString(3, author.getNationality());
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    author.setId(resultSet.getInt(1));
                }
            }
        }
        return author;
    }

    @Override
    public void update(Author author) throws SQLException {
        String sql = "UPDATE authors SET name = ?, birthYear = ?, nationality = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getBirthYear());
            preparedStatement.setString(3, author.getNationality());
            preparedStatement.setInt(4, author.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private Author mapResultSetToAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt("id"));
        author.setName(resultSet.getString("name"));
        author.setBirthYear(resultSet.getInt("birth_Year"));
        author.setNationality(resultSet.getString("nationality"));
        return author;
    }
}