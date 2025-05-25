package main.java.dao.impl;

import main.java.dao.BookDao;
import main.java.dao.DatabaseConnection;
import main.java.model.Book;
import java.sql.*;
import java.util.*;

public class BookDaoImpl implements BookDao {

    private static final String FIND_BOOKS_BY_AUTHOR =
            "SELECT b.* FROM books b " +
                    "JOIN authors a ON b.author_id = a.id " +
                    "WHERE a.name ILIKE ?";

    private static final String FIND_AVAILABLE_BOOKS =
            "SELECT * FROM books WHERE is_available = true";

    private static final String FIND_BY_ID = "SELECT * FROM books WHERE id = ?";

    private static final String SAVE =
            "INSERT INTO books (title, author_id, publication_year, genre, pages, is_available) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE books SET title = ?, author_id = ?, publication_year = ?, " +
                    "genre = ?, pages = ?, is_available = ? WHERE id = ?";

    private static final String DELETE_BY_ID = "DELETE FROM books WHERE id = ?";

    private static final String BORROW_BOOK =
            "UPDATE books SET is_available = false WHERE id = ? AND is_available = true";

    private static final String RETURN_BOOK =
            "UPDATE books SET is_available = true WHERE id = ? AND is_available = false";

    private static final String GET_BOOK_STATISTICS =
            "SELECT genre, COUNT(*) as count FROM books GROUP BY genre";

    private static final String CHECK_BOOK_EXISTS =
            "SELECT is_available FROM books WHERE id = ?";

    @Override
    public List<Book> findBookByAuthor(String authorName) throws SQLException {
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BOOKS_BY_AUTHOR)) {

            ps.setString(1, "%" + authorName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(createBookFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding books by author: " + e.getMessage());
            throw e;
        }

        return books;
    }

    @Override
    public List<Book> findAvailableBooks(int limit) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = FIND_AVAILABLE_BOOKS;

        if (limit > 0) {
            query += " LIMIT ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            if (limit > 0) {
                ps.setInt(1, limit);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(createBookFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding available books: " + e.getMessage());
            throw e;
        }

        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createBookFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding book by ID: " + e.getMessage());
            throw e;
        }

        return Optional.empty();
    }

    @Override
    public Book save(Book book) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());

            if (book.getPublicationYear() != null) {
                ps.setInt(3, book.getPublicationYear());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, book.getGenre());

            if (book.getPages() != null) {
                ps.setInt(5, book.getPages());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setBoolean(6, book.isAvailable());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Book saved successfully: " + book.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error saving book: " + e.getMessage());
            throw e;
        }

        return book;
    }


    @Override
    public void update(Book book) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, book.getTitle());
            ps.setInt(2, book.getAuthorId());

            if (book.getPublicationYear() != null) {
                ps.setInt(3, book.getPublicationYear());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            ps.setString(4, book.getGenre());

            if (book.getPages() != null) {
                ps.setInt(5, book.getPages());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            ps.setBoolean(6, book.isAvailable());
            ps.setInt(7, book.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book updated successfully: " + book.getTitle());
            } else {
                System.out.println("No book found with ID: " + book.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
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
                System.out.println("Book deleted successfully with ID: " + id);
            } else {
                System.out.println("No book found with ID: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void borrowBook(int bookId) throws SQLException {
        if (!isBookAvailable(bookId)) {
            throw new RuntimeException("Book with ID " + bookId + " is not available or doesn't exist");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(BORROW_BOOK)) {

            ps.setInt(1, bookId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book borrowed successfully. Book ID: " + bookId);
            } else {
                throw new RuntimeException("Failed to borrow book with ID: " + bookId);
            }
        } catch (SQLException e) {
            System.err.println("Error borrowing book: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void returnBook(int bookId) throws SQLException {
        if (!doesBookExist(bookId)) {
            throw new RuntimeException("Book with ID " + bookId + " doesn't exist");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(RETURN_BOOK)) {

            ps.setInt(1, bookId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Book returned successfully. Book ID: " + bookId);
            } else {
                System.out.println("Book with ID " + bookId + " was already available");
            }
        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        Map<String, Long> statistics = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BOOK_STATISTICS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String genre = rs.getString("genre");
                long count = rs.getLong("count");
                statistics.put(genre, count);
            }
        } catch (SQLException e) {
            System.err.println("Error getting book statistics: " + e.getMessage());
            throw e;
        }

        return statistics;
    }

    // Helper methods
    private Book createBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(rs.getInt("author_id"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setGenre(rs.getString("genre"));
        book.setPages(rs.getInt("pages"));
        book.setAvailable(rs.getBoolean("is_available"));

        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            book.setCreatedAt(timestamp.toLocalDateTime());
        }

        return book;
    }

    private boolean isBookAvailable(int bookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_BOOK_EXISTS)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_available");
                }
            }
        }
        return false;
    }

    private boolean doesBookExist(int bookId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_BOOK_EXISTS)) {

            ps.setInt(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}