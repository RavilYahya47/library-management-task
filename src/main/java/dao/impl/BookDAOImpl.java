package java.dao.impl;

import java.dao.BookDAO;
import java.dao.DatabaseConnection;
import java.model.Book;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<Book>();
        String query = "SELECT * FROM books";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int publicationYear = resultSet.getInt("publication_year");
                String genre = resultSet.getString("genre");
                int pages = resultSet.getInt("pages");
                Boolean isAvailable = resultSet.getBoolean("is_available");
                Timestamp ts = resultSet.getTimestamp("created_at");
                LocalDateTime ldt = ts.toLocalDateTime();

                Book book = new Book(id,title,authorId,publicationYear,genre,pages, isAvailable, ldt);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("There is a problem connection to DB : " + e.getMessage());
        }

        return books;
    }

    @Override
    public Optional<Book> findById(int id) {
        String query = "SELECT * FROM books WHERE id=?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1,id);
            int i = preparedStatement.executeUpdate();
            if(i>0) {
                return Optional.of(Book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book save(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author_id, publication_year, genre, pages) values() ";
    }

    @Override
    public void update(Book book) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {

    }
}
