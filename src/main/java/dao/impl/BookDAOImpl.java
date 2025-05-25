package main.java.dao.impl;

import main.java.dao.BookDAO;
import main.java.dao.DatabaseConnection;
import main.java.exceptions.BookNotFoundException;
import main.java.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM task.books";

        try(Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            while(rs.next()){
                Book currentBook = new Book(rs.getInt(1), rs.getString(2),
                        rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getBoolean(7));
                books.add(currentBook);
            }
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        String query = "SELECT * FROM task.books WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return Optional.of(new Book(rs.getInt(1), rs.getString(2),
                        rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getBoolean(7)));
            } else{
                return Optional.empty();
            }
        }
    }

    @Override
    public Book save(Book book) throws SQLException {
        String query = "INSERT INTO task.books (title, author_id, publication_year, genre, pages, is_available) \n" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String query2 = "SELECT id FROM task.books WHERE title = ? AND author_id = ? AND publication_year = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            PreparedStatement statement2 = connection.prepareStatement(query2)){

            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getAuthorId());
            statement.setInt(3, book.getPublicationYear());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPages());
            statement.setBoolean(6, book.isAvailable());

            int added = statement.executeUpdate();
            if(added > 0){
                System.out.println("Book added successfully!");
                statement2.setString(1, book.getTitle());
                statement2.setInt(2, book.getAuthorId());
                statement2.setInt(3, book.getPublicationYear());

                ResultSet rs = statement2.executeQuery();
                rs.next();
                book.setId(rs.getInt(1));
            } else{
                book.setId(-1);
                System.out.println("Could not add the book!");
            }
        }
        return book;
    }
    @Override
    public void update(Book book) throws SQLException {
        String query = """
                UPDATE task.books\s
                SET title = ?, author_id = ?, publication_year = ?, genre = ?, pages = ?, is_available = ?\s
                WHERE id = ?""";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getAuthorId());
            statement.setInt(3, book.getPublicationYear());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPages());
            statement.setBoolean(6, book.isAvailable());
            statement.setInt(7, book.getId());


            int updated = statement.executeUpdate();
            if (updated == 1) {
                System.out.println("Updated successfully!");
            } else {
                throw new BookNotFoundException("Book by id = " + book.getId() + " not found!");
            }
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM task.books WHERE id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            int deleted = statement.executeUpdate();
            if(deleted == 1){
                System.out.println("Deleted successfully!");
            } else{
                throw new BookNotFoundException("Book by id = " + id + " not found!");
            }
        }
    }
}
