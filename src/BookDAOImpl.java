import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    private final Connection connection = DatabaseConnection.getConnection();

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = mapResultSetToBook(resultSet);
                books.add(book);
            }
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Kitab tapılarkən xəta baş verdi.", e);
        }
    }

    @Override
    public Book save(Book book) throws SQLException {
        String sql = "INSERT INTO books(id, title, authorId, publicationYear, genre, pages, isAvailable, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setInt(3, book.getAuthorId());
            statement.setInt(4, book.getPublicationYear());
            statement.setString(5, book.getGenre());
            statement.setInt(6, book.getPages());
            statement.setBoolean(7, book.isAvailable());
            statement.setTimestamp(8, Timestamp.valueOf(book.getCreatedAt()));

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return book;
            } else {
                throw new RuntimeException("Kitab bazaya əlavə olunmadı.");
            }
        }
    }

    @Override
    public void update(Book book) throws SQLException {
        String sql = "UPDATE books SET title = ?, authorId = ?, publicationYear = ?, genre = ?, pages = ?, isAvailable = ?, createdAt = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getAuthorId());
            statement.setInt(3, book.getPublicationYear());
            statement.setString(4, book.getGenre());
            statement.setInt(5, book.getPages());
            statement.setBoolean(6, book.isAvailable());
            statement.setTimestamp(7, Timestamp.valueOf(book.getCreatedAt()));
            statement.setInt(8, book.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kitab uğurla yeniləndi.");
            } else {
                throw new RuntimeException("Kitab tapılmadı və yenilənmədi.");
            }
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Kitab uğurla bazadan silindi.");
            } else {
                throw new BookNotFoundException("Kitab bazada taplimadi ya silinib ya da umumiyyetle bazada olmayib");
            }
        }
    }

    private Book mapResultSetToBook(ResultSet resultSet) {
        try {
            Book book = new Book();
            book.setId(resultSet.getInt("id"));
            book.setTitle(resultSet.getString("title"));
            book.setAuthorId(resultSet.getInt("author_id"));
            book.setPublicationYear(resultSet.getInt("publication_year"));
            book.setGenre(resultSet.getString("genre"));
            Timestamp timestamp = resultSet.getTimestamp("created_at");
            if (timestamp != null) {
                book.setCreatedAt(timestamp.toLocalDateTime());
            }
            book.setPages(resultSet.getInt("pages"));
            book.setAvailable(resultSet.getBoolean("is_available"));
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("ResultSet Book obyektinə çevrilərkən xəta baş verdi.", e);
        }
    }
}
