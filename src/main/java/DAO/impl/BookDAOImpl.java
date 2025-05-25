package main.java.DAO.impl;

import main.java.DAO.BookDAO;
import main.java.model.Book;
import main.java.DAO.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToBook(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Book save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author_id, publication_year, genre, pages, is_available) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setObject(3, book.getPublicationYear(), Types.INTEGER);
            stmt.setString(4, book.getGenre());
            stmt.setObject(5, book.getPages(), Types.INTEGER);
            stmt.setBoolean(6, book.isAvailable());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                book.setId(rs.getInt("id"));
            }
        }
        return book;
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, author_id = ?, publication_year = ?, genre = ?, pages = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setObject(3, book.getPublicationYear(), Types.INTEGER);
            stmt.setString(4, book.getGenre());
            stmt.setObject(5, book.getPages(), Types.INTEGER);
            stmt.setBoolean(6, book.isAvailable());
            stmt.setInt(7, book.getId());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Book> findByAuthorName(String authorName) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b JOIN authors a ON b.author_id = a.id WHERE a.name ILIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + authorName + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        }
        return books;
    }

    @Override
    public List<Book> findAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_available = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        }
        return books;
    }

    @Override
    public List<Book> findByGenre(String genre) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE genre ILIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(mapRowToBook(rs));
            }
        }
        return books;
    }

    private Book mapRowToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("author_id"),
                rs.getObject("publication_year", Integer.class),
                rs.getString("genre"),
                rs.getObject("pages", Integer.class),
                rs.getBoolean("is_available"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}