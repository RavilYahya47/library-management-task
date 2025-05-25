package main.java.dao.impl;

import main.java.dao.BookDAO;
import main.java.dao.DatabaseConnection;
import main.java.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet));
            }
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToBook(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Book save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, authorId, publicationYear, genre, pages, isAvailable, createdAt) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getAuthorId());
            preparedStatement.setInt(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setInt(5, book.getPages());
            preparedStatement.setBoolean(6, book.isAvailable());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(book.getCreatedAt()));

            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    book.setId(resultSet.getInt(1));
                }
            }
        }
        return book;
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, authorId = ?, publicationYear = ?, genre = ?, pages = ?, " + "isAvailable = ?, createdAt = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getAuthorId());
            preparedStatement.setInt(3, book.getPublicationYear());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setInt(5, book.getPages());
            preparedStatement.setBoolean(6, book.isAvailable());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(book.getCreatedAt()));
            preparedStatement.setInt(8, book.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private Book mapResultSetToBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthorId(resultSet.getInt("author_Id"));
        book.setPublicationYear(resultSet.getInt("publication_Year"));
        book.setGenre(resultSet.getString("genre"));
        book.setPages(resultSet.getInt("pages"));
        book.setAvailable(resultSet.getBoolean("is_Available"));
        book.setCreatedAt(resultSet.getTimestamp("created_At").toLocalDateTime());
        return book;
    }
}