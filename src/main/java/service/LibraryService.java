package java.service;

import java.dao.AuthorDAO;
import java.dao.BookDAO;
import java.dao.DatabaseConnection;
import java.model.Book;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;

    public LibraryService(AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    // Constructor dependency injection

    // Business logic methodları:
    public List<Book> findBooksByAuthor(String authorName) {
        List<Book> books = new java.util.ArrayList<>();
        String query = "SELECT b.* FROM books b  " +
                "JOIN authors a ON b.author_id = a.id" + "WHERE a.name ILIKE %?%";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query))
        {
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int publicatinYear = resultSet.getInt("publication_year");
                String genre = resultSet.getString("genre");
                int pages = resultSet.getInt("pages");
                Boolean isAvailable = resultSet.getBoolean("is_available");
                Timestamp ts = resultSet.getTimestamp("created_at");
                LocalDateTime ldt = ts.toLocalDateTime();
                books.add(new Book(id,title,authorId,publicatinYear,genre,pages,isAvailable,ldt));
                return books;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;

    }
    public List<Book> findAvailableBooks(){
        List<Book> books = new ArrayList<>();
        String query = "SELECT books id, title, author_id, publication_year, genre, pages, is_available, created_at FROM books WHERE isAvailable=true";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query))
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                int authorId = resultSet.getInt("author_id");
                int publicatinYear = resultSet.getInt("publication_year");
                String genre = resultSet.getString("genre");
                int pages = resultSet.getInt("pages");
                Boolean isAvailable = resultSet.getBoolean("is_available");
                Timestamp ts = resultSet.getTimestamp("created_at");
                LocalDateTime ldt = ts.toLocalDateTime();
                Book book = new Book(id,title,authorId,publicatinYear,genre,pages,isAvailable,ldt);
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("There is a connection problem : " + e.getMessage());
        }
        return books;
    }
    public void borrowBook(int bookId) throws SQLException{

    }

    public void returnBook(int bookId) {
        String query = "UPDATE books SET is_available = true WHERE id = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, bookId);
            int rowAffacted = preparedStatement.executeUpdate();

            if (rowAffacted >0) {
                System.out.println(bookId + " - bookId is updated");
            } else {
                System.out.println("Do not Update");
            }
        }
        catch (SQLException e) {
            System.out.println("There is a connection problem : " + e.getMessage());
        }
    }
    public Map<String, Long> getBookStatisticsByGenre(){
        Map<String, Long> map = new HashMap<>();
        String query = "SELECT genre, COUNT(*) AS total FROM books GROUP BY genre";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                long total = resultSet.getLong("total");
                map.put(genre, total);
            }
        } catch (SQLException e) {
            System.out.println("There is a connection problem: " + e.getMessage());
        }
        return map;
     }
}
