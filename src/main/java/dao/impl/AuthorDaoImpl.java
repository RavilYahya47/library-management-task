package main.java.dao.impl;

import main.java.dao.AuthorDao;
import main.java.dao.DatabaseConnection;
import main.java.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDao {

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mapToAuthor(rs).ifPresent(authors::add);
            }

        } catch (SQLException e) {
            System.err.println("findAll zamanı xeta: " + e.getMessage());
        }

        return authors;
    }


    @Override
    public Optional<Author> findById(int id) {
        String query = "SELECT * FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Optional<Author> author = mapToAuthor(rs);
                    if (author.isPresent()) {
                        return author;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Author save(Author author) {
        String query = "INSERT INTO authors (name) VALUES (?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, author.getName());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    author.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Save zamani xeta: " + e.getMessage());
        }

        return author;
    }


    @Override
    public void update(Author author) {
        String query = "UPDATE authors SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, author.getName());
            ps.setInt(2, author.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Update zamani xeta: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM authors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Silme zamani xeta: " + e.getMessage());
        }
    }

    public Optional<Author> mapToAuthor(ResultSet rs) {
        try {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            return Optional.of(author);
        } catch (SQLException e) {
            System.err.println("Xeta: " + e.getMessage());
            return Optional.empty();
        }
    }
}
