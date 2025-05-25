package main.java.DAO.impl;

import main.java.DAO.AuthorDAO;
import main.java.DAO.DatabaseConnection;
import main.java.model.Author;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("birth_year"),
                        rs.getString("nationality")
                );
                authors.add(author);
            }
        }
        return authors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        String query = "SELECT * FROM authors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Author author = new Author(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("birth_year"),
                        rs.getString("nationality")
                );
                return Optional.of(author);
            }
        }
        return Optional.empty();
    }

    @Override
    public Author save(Author author) throws SQLException {
        String query = "INSERT INTO authors (name, birth_year, nationality) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getBirthYear());
            stmt.setString(3, author.getNationality());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                author.setId(rs.getInt("id"));
            }
        }
        return author;
    }

    @Override
    public void update(Author author) throws SQLException {
        String query = "UPDATE authors SET name = ?, birth_year = ?, nationality = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getBirthYear());
            stmt.setString(3, author.getNationality());
            stmt.setInt(4, author.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM authors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
