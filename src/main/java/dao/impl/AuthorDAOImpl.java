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
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseConnection.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int birth_year = resultSet.getInt(3);
                String nationality = resultSet.getString(4);
                Author author = new Author(id,name,birth_year,nationality);
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return authors;
    }

    @Override
    public Optional<Author> findById(int id) {
        String query = "SELECT * FROM authors WHERE id = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Author> optionalAuthor = Optional.empty();
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
            return Optional.empty();
        }
    }

    @Override
    public Author save(Author author) throws SQLException {
        String query = "INSERT INTO authors (ID,name, birth_year, nationality) VALUES (?, ?, ?,?)";
        try (PreparedStatement preperedStatement = DatabaseConnection.getConnection().prepareStatement(query) ){
            preperedStatement.setInt(1, author.getId());
            preperedStatement.setString(2, author.getName());
            preperedStatement.setInt(3, author.getBirthYear());
            preperedStatement.setString(4, author.getNationality());

            int rowAffected = preperedStatement.executeUpdate();
            if (rowAffected>0) {
                return author;
            } else {
                System.out.println("Author is not added");
            }
        } catch (SQLException e) {
            System.out.println("There is problem:  " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public void update(Author author) {
        String query = "UPDATE authors SET  name = ?, birth_year=?, nationality=? WHERE id=?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getBirthYear());
            preparedStatement.setString(3, author.getNationality());
            preparedStatement.setInt(4, author.getId());
            int i = preparedStatement.executeUpdate();
            if (i>0) {
                System.out.println("Update query is successfully");
            }else System.out.println("Do not update");
        } catch (SQLException e) {
            System.out.println("There is a connection problem : " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM authors WHERE ID=?";
        try (
            PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query);) {
            preparedStatement.setInt(1, id);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected>0) {
                System.out.println("Deleted query is successfully");
            }
            else{
                System.out.println("Do not delete");
            }
        } catch (SQLException e) {
            System.out.println("Do not delete student: " + e.getMessage());
        }

    }
}
