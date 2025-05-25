package main.java.service;

import main.java.dao.AuthorDAO;
import main.java.dao.BookDAO;
import main.java.exception.AuthorNotFoundException;
import main.java.exception.BookNotAvailableException;
import main.java.exception.BookNotFoundException;
import main.java.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    private Connection connection;

    // Constructor dependency injection
    public LibraryService(AuthorDAO authorDAO, BookDAO bookDAO, Connection connection) {
        this.authorDAO = authorDAO;
        this.bookDAO = bookDAO;
        this.connection = connection;

    }

    public List<Book> findBooksByAuthor(String authorName) {
        List<Book> books = new ArrayList<>();

        // Getting author's id at the given author's name
        String getAuthorId = "SELECT id FROM authors WHERE name = ?";

        // Selecting books at the getting author's id
        String getBooksByAuthorId = "SELECT * FROM books WHERE author_id = ?";

        try (var stmt1 = connection.prepareStatement(getAuthorId);
             var stmt2 = connection.prepareStatement(getBooksByAuthorId)) {

            stmt1.setString(1, authorName);

            ResultSet set1 = stmt1.executeQuery();

            if (!set1.next()) {
                throw new AuthorNotFoundException("Author not found: " + authorName);
            } else {
                int id = set1.getInt("id");

                System.out.println(id);
                stmt2.setInt(1, id);

                ResultSet set2 = stmt2.executeQuery();
                while (set2.next()) {
                    int bookId = set2.getInt("id");
                    String title = set2.getString("title");
                    int authorId = set2.getInt("author_id");
                    Integer publicationYear = set2.getInt("publication_year");
                    String genre = set2.getString("genre");
                    Integer pages = set2.getInt("pages");
                    boolean isAvailable = set2.getBoolean("is_available");
                    LocalDateTime createdAt = set2.getObject("created_at", LocalDateTime.class);

                    Book book = new Book(bookId, title, authorId, publicationYear, genre, pages, isAvailable, createdAt);

                    books.add(book);
                }
                return books;
            }
        } catch (AuthorNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Book> findAvailableBooks(Predicate<Book> condition) {
        List<Book> availableBooks = new ArrayList<>();

        try {
            List<Book> books = bookDAO.findAll();

            books
                    .stream()
                    .filter(condition)
                    .forEach(availableBooks::add);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return availableBooks;
    }

    public void borrowBook(int bookId) throws SQLException {
        List<Book> books = bookDAO.findAll();

        boolean isFound = false;

        for (Book book : books) {
            if (book.getId() == bookId) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            // Getting is_available column value at the given book id
            String isAvailableQuery = "SELECT is_available FROM books WHERE id = ?";

            // Updating database is_available column value at the given book id
            String query = "UPDATE books SET is_available = false WHERE id = ?";

            try (var stmt1 = connection.prepareStatement(query);
                 var stmt2 = connection.prepareStatement(isAvailableQuery)) {
                stmt1.setInt(1, bookId);
                stmt2.setInt(1, bookId);

                ResultSet resultSet = stmt2.executeQuery();

                resultSet.next();

                boolean isAvailable = resultSet.getBoolean("is_available");

                if (isAvailable) {
                    int affectedRow = stmt1.executeUpdate();
                    System.out.println("Affected row: " + affectedRow);
                    System.out.println("Book borrowed...");
                } else {
                    throw new BookNotAvailableException("Sorry, book is already borrowed...");
                }
            } catch (BookNotFoundException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid id!");
        }

    }

    public void returnBook(int bookId) throws SQLException {
        List<Book> books = bookDAO.findAll();

        boolean isFound = false;

        for (Book book : books) {
            if (book.getId() == bookId) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            // Updating is_available column value at the given id
            String query = "UPDATE books SET is_available = true WHERE id = ?";

            try (var stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, bookId);

                int affectedRow = stmt.executeUpdate();
                System.out.println("Affected row: " + affectedRow);
                System.out.println("Book is returned...");

            } catch (BookNotFoundException | SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid id!");
        }

    }

    public Map<String, Long> getBookStatisticsByGenre() {
        Map<String, Long> map = new HashMap<>();
        try {
            map = bookDAO.findAll()
                    .stream()
                    .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return map;
    }
}
