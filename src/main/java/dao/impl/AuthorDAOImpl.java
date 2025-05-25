package main.java.dao.impl;

import main.java.dao.AuthorDAO;
import main.java.dao.DatabaseConnection;
import main.java.exceptions.AuthorNotFoundException;
import main.java.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {
    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM task.authors";

         try(Connection connection = DatabaseConnection.getConnection();
         Statement statement = connection.createStatement();
         ResultSet rs = statement.executeQuery(query)){
             while(rs.next()){
                 Author currentAuthor = new Author(rs.getInt(1), rs.getString(2),
                         rs.getInt(3), rs.getString(4));
                 authors.add(currentAuthor);
             }
         }
         return authors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        String query = "SELECT * FROM task.authors WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                return Optional.of(new Author(rs.getInt(1), rs.getString(2),
                        rs.getInt(3), rs.getString(4)));
            } else{
                return Optional.empty();
            }
        }
    }

    @Override
    public Author save(Author author) throws SQLException {
        String query = "INSERT INTO task.authors (name, birth_year, nationality) \n" +
                "VALUES (?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, author.getName());
            statement.setInt(2, author.getBirthYear());
            statement.setString(3, author.getNationality());

            int added = statement.executeUpdate();
            if(added > 0){
                System.out.println("Author added successfully!");
                author.setId(-1);
            } else{
                System.out.println("Could not add the author!");
            }
            return author;
        }

    }

    @Override
    public void update(Author author) throws SQLException {
        String query = """
                UPDATE task.authors\s
                SET name = ?, birth_year = ?, nationality = ?\s
                WHERE id = ?""";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, author.getName());
            statement.setInt(2, author.getBirthYear());
            statement.setString(3, author.getNationality());
            statement.setInt(4, author.getId());

            int updated = statement.executeUpdate(query);
            if(updated == 1){
                System.out.println("Updated successfully!");
            } else{
                throw new AuthorNotFoundException("Author by id = " + author.getId() + " not found!");
            }
        }
}

    @Override
    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM task.authors WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            int deleted = statement.executeUpdate();
            if(deleted == 1){
                System.out.println("Deleted successfully!");
            } else{
                throw new AuthorNotFoundException("Author by id = " + id + " not found!");
            }
        }
    }
}
