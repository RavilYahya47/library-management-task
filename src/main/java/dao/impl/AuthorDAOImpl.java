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
        String query = "SELECT * FROM author WHERE id = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
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
        String query = "INSERT INTO authors (ID,name, birth_year, nationality) VALUES (?, ?, ?)";
        try (PreparedStatement preperedStatement = DatabaseConnection.getConnection().prepareStatement(query) ){
            preperedStatement.setInt(1, author.getId());
            preperedStatement.setString(2, author.getName());
            preperedStatement.setInt(3, author.getBirthYear());
            preperedStatement.setString(4, author.getNationality());
            ResultSet result = preperedStatement.executeQuery();
            Author savedAuthor;
            while (result.next()) {
                int ID = result.getInt(1);
                String name = result.getString(2);
                int birth_year = result.getInt(3);
                String nationality = result.getString(4);
                savedAuthor = new Author(ID, name, birth_year, nationality);
                return savedAuthor;
            }
        } catch (SQLException e) {
            System.out.println("There is problem:  " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public void update(Author author) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM authors WHERE ID=?";
        try {
            PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected>0) {
                System.out.println("Deleted");
            }
            else{
                System.out.println("not deleted");
            }
        } catch (SQLException e) {
            System.out.println("Do not delete student: " + e.getMessage());
        }

    }
}
