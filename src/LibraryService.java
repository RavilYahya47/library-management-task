import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {
    Connection connection = DatabaseConnection.getConnection();

    public LibraryService(AuthorDAO authorDAO, BookDAO bookDAO) {
    }

    public List<Book> findBooksByAuthor(Integer author_id) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT*FROM books WHERE author_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, author_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorId(rs.getInt("author_id"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setGenre(rs.getString("genre"));
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    book.setCreatedAt(timestamp.toLocalDateTime());
                }
                books.add(book);
            }
        }
        return books;
    }

    public LibraryService() {
    }

    public List<Book> findAvailableBooks() throws SQLException {
        String sql = "SELECT * FROM books WHERE is_available = true";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Book book1 = new Book();
                book1.setId(rs.getInt("id"));
                book1.setTitle(rs.getString("title"));
                book1.setAuthorId(rs.getInt("author_id"));
                book1.setPublicationYear(rs.getInt("publication_year"));
            }
        }
        return books;
    }

    public void borrowBook(int bookId) throws SQLException {
        String sql = "UPDATE books SET is_available = false WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kitab icareye hazir veziyyete getirildi");
            } else throw new BookNotFoundException("Bu kitap icarilmadi ya silinib ya da umumiyyetle icarilmadi");
        }
    }

    public void returnBook(int bookId) throws SQLException {
        String sql = "UPDATE books SET is_available = true WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kitab kitab ugurla qaytarildi");
            } else throw new BookNotFoundException("Bu kitap ugurla qaytarildi");
        }
    }


    public Map<String, Long> getBookStatisticsByGenre() throws SQLException {
        Map<String, Long> statistics = new HashMap<>();
        String sql = "SELECT genre, COUNT(*) FROM books GROUP BY genre";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                statistics.put(rs.getString("genre"), rs.getLong("count"));
            }
        }
        return statistics;

    }

    public static Map<Integer, String> getAllOperations() {
        Map<Integer, String> operations = new HashMap<>();
        operations.put(1, "Bütün kitabları göstər");
        operations.put(2, "Müəllifə görə kitab axtar");
        operations.put(3, "Mövcud kitabları göstər");
        operations.put(4, "Kitab icarəyə ver");
        operations.put(5, "Kitabı qaytart");
        operations.put(6, "Janr üzrə statistika");
        operations.put(0, "Çıxış");
        return operations;
    }
}
