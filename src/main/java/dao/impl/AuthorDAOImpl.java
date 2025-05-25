package main.java.dao.impl;

import main.java.dao.AuthorDAO;
import main.java.exception.AuthorNotFoundException;
import main.java.model.Author;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    private Connection connection;

    public AuthorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();

        // Getting all authors from database
        String query = "SELECT * FROM task.authors";
        try (var stmt = connection.prepareStatement(query)) {
            ResultSet allAuthors = stmt.executeQuery();

            while (allAuthors.next()) {
                int id = allAuthors.getInt("id");
                String name = allAuthors.getString("name");
                Integer birthYear = allAuthors.getInt("birth_year");
                String nationality = allAuthors.getString("nationality");

                Author author = new Author(id, name, birthYear, nationality);

                authors.add(author);

            }
        } catch (SQLException e) {
            System.out.println("Error has occurred during selecting authors");
        }
        return authors;
    }

    @Override
    public Optional<Author> findById(int id) {
        Optional<Author> chosenAuthor = Optional.empty();

        try {
            List<Author> authors = findAll();

            chosenAuthor = authors
                    .stream()
                    .filter(author -> author.getId() == id)
                    .findAny();
        } catch (AuthorNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return chosenAuthor;
    }

    @Override
    public Author save(Author author) {

        // Adding new author to database
        String query = "INSERT INTO task.authors (name, birth_year, nationality) VALUES (?, ?, ?)";

        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getBirthYear());
            stmt.setString(3, author.getNationality());

            int affectedRows = stmt.executeUpdate();
            System.out.println(affectedRows + " row affected");

            List<Author> authors = findAll();
            int id;
            for (Author author1 : authors) {
                if (author1.getName().equals(author.getName())
                        && Objects.equals(author1.getBirthYear(), author.getBirthYear())
                        && author1.getNationality().equals(author.getNationality())) {

                    return author1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during saving author");
        }
        return null;
    }

    @Override
    public void update(Author author) {

        // Updating an author from database
        String query = "UPDATE task.authors SET name = ?, birth_year = ?, nationality = ? WHERE id = ?";

        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getName());
            stmt.setInt(2, author.getBirthYear());
            stmt.setString(3, author.getNationality());

            stmt.executeUpdate();

        } catch (AuthorNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {

        // Deleting author at the given id from database
        String query = "DELETE from task.authors WHERE id = ?";

        try (var stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (AuthorNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
