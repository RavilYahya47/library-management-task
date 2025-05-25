package main.java.dao.impl;

import main.java.dao.BookDao;
import main.java.dao.DatabaseConnection;
import main.java.model.Book;

import java.sql.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                books.add(mapToBook(rs));
            }

        } catch (SQLException e) {
            System.err.println(": " + e.getMessage());
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) {
        String query = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToBook(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("ID ye gore tapilmada xeta: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Book save(Book book) {
        String query = "INSERT INTO books (title, author_id, publication_year, genre, pages, is_available, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // 1-4: Mətn
            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());
            ps.setObject(3, book.getPublicationYear(), Types.INTEGER); // null da ola bilər
            ps.setString(4, book.getGenre());
            ps.setObject(5, book.getPages(), Types.INTEGER); // null da ola bilər

            // 6: boolean dəyər
            ps.setBoolean(6, book.isAvailable());

            // 7: created_at - null dirsa, indiki vaxt qoy
            LocalDateTime createdAt = book.getCreatedAt() != null ? book.getCreatedAt() : LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(createdAt));

            // INSERT neticesinde qaytarilan ID-ni gotur
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    book.setId(rs.getInt("id"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Save zamani xeta: " + e.getMessage());
        }

        return book;
    }

    @Override
    public void update(Book book) {
        String query = "UPDATE books SET title = ?, author_id = ?, publication_year = ?, genre = ?, pages = ?, is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());
            ps.setObject(3, book.getPublicationYear(), java.sql.Types.INTEGER);
            ps.setString(4, book.getGenre());
            ps.setObject(5, book.getPages(), java.sql.Types.INTEGER);
            ps.setBoolean(6, book.isAvailable());
            ps.setInt(7, book.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Update zamani xeta: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Silme zamani xeta: " + e.getMessage());
        }
    }

    public Book mapToBook(ResultSet rs) throws SQLException {
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
