package main.java.dao.impl;

import main.java.dao.AuthorDao;
import main.java.dao.DatabaseConnection;
import main.java.model.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDaoImpl implements AuthorDao {

    private static final String FIND_ALL = "SELECT * FROM authors";
    private static final String FIND_BY_ID = "SELECT * FROM authors WHERE id = ?";
    private static final String SAVE = "INSERT INTO authors (name, birth_year, nationality) VALUES(?, ?, ?)";
    private static final String DELETE_BY_ID = "DELETE FROM authors WHERE id = ?";
    private static final String UPDATE = "UPDATE authors SET name = ?, birth_year = ?, nationality = ? WHERE id = ?";

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> allAuthors = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL)) {

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setBirthYear(resultSet.getInt("birth_year"));
                author.setNationality(resultSet.getString("nationality"));
                allAuthors.add(author);
            }
        } catch (SQLException e) {
            System.err.println("Error in findAll: " + e.getMessage());
            throw e;
        }

        return allAuthors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    Author author = new Author();
                    author.setId(resultSet.getInt("id"));
                    author.setName(resultSet.getString("name"));
                    author.setBirthYear(resultSet.getInt("birth_year"));
                    author.setNationality(resultSet.getString("nationality"));

                    return Optional.of(author);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findById: " + e.getMessage());
            throw e;
        }

        return Optional.empty();
    }

    @Override
    public Author save(Author author) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, author.getName());
            ps.setInt(2, author.getBirthYear());
            ps.setString(3, author.getNationality());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        author.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Author Created: " + author.toString());
            }
        } catch (SQLException e) {
            System.err.println("Error in save: " + e.getMessage());
            throw e;
        }

        return author;
    }

    @Override
    public void update(Author author) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, author.getName());
            ps.setInt(2, author.getBirthYear());
            ps.setString(3, author.getNationality());
            ps.setInt(4, author.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Author Updated: " + author.toString());
            } else {
                System.out.println("No Author found with ID: " + author.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error in update: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_ID)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Author Deleted with ID: " + id);
            } else {
                System.out.println("No Author found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error in deleteById: " + e.getMessage());
            throw e;
        }
    }
}