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
import java.util.List;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;

    public LibraryService(AuthorDAO authorDAO, BookDAO bookDAO) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
    }

    // Constructor dependency injection

    // Business logic methodları:
    public List<Book> findBooksByAuthor(String authorName) throws SQLException {
        List<Book> books = new java.util.ArrayList<>();


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
    public void returnBook(int bookId) throws SQLException{

    }
    public Map<String, Long> getBookStatisticsByGenre() throws SQLException{

    }
}
