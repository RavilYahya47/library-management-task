package main.java.dao.impl;

import main.java.dao.BookDAO;
import main.java.exception.BookNotFoundException;
import main.java.model.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {

    private Connection connection;

    public BookDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        // Getting all books from database
        String query = "SELECT * FROM task.books";
        try (var stmt = connection.prepareStatement(query)) {
            ResultSet allBooks = stmt.executeQuery();

            while (allBooks.next()) {
                int id = allBooks.getInt("id");
                String title = allBooks.getString("title");
                int authorId = allBooks.getInt("author_id");
                Integer publicationYear = allBooks.getInt("publication_year");
                String genre = allBooks.getString("genre");
                Integer pages = allBooks.getInt("pages");
                boolean isAvailable = allBooks.getBoolean("is_available");
                LocalDateTime createdAt = allBooks.getObject("created_at", LocalDateTime.class);

                Book book = new Book(id, title, authorId, publicationYear, genre, pages, isAvailable, createdAt);

                books.add(book);

            }
        } catch (SQLException e) {
            System.out.println("Error has occurred during selecting books");
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        Optional<Book> chosenBook = Optional.empty();

        try {
            List<Book> books = findAll();

            chosenBook = books
                    .stream()
                    .filter(book -> book.getId() == id)
                    .findAny();
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return chosenBook;
    }

    @Override
    public Book save(Book book) throws SQLException {

        // Adding new book to database
        String query = "INSERT INTO task.books (title, author_id, publication_year, genre, pages, is_available) VALUES(?, ?, ?, ?, ?, ?)";

        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getPublicationYear());
            stmt.setString(4, book.getGenre());
            stmt.setInt(5, book.getPages());
            stmt.setBoolean(6, book.getIsAvailable());

            int affectedRows = stmt.executeUpdate();
            System.out.println(affectedRows + " row affected");

            List<Book> books = findAll();
            int id;
            for (Book book1 : books) {
                if (book1.getTitle().equals(book.getTitle())
                        && book1.getPublicationYear().equals(book.getPublicationYear())
                        && book1.getPages().equals(book.getPages())
                        && book1.getAuthorId() == book.getAuthorId()
                        && book1.getGenre().equals(book.getGenre())
                        && book1.getIsAvailable() == book.getIsAvailable()) {

                    return book1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error occurred during saving book");
        }
        return null;
    }

    @Override
    public void update(Book book) throws SQLException {

        // Updating a book from database
        String query = "UPDATE task.books SET title = ?, author_id = ?, publication_year = ?, genre = ?, pages = ?, is_available = ? WHERE id = ?";

        try (var stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getAuthorId());
            stmt.setInt(3, book.getPublicationYear());
            stmt.setString(4, book.getGenre());
            stmt.setInt(5, book.getPages());
            stmt.setBoolean(6, book.getIsAvailable());

            stmt.executeUpdate();

        } catch (BookNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        // Deleting book at the given id from database
        String query = "DELETE from task.books WHERE id = ?";

        try (var stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (BookNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void allBooksWithTable(List<Book> books) {
        System.out.printf("%n%-5s %-35s %-15s %-25s %-20s %-15s %-15s %-20s%n",
                "ID", "Title", "Author ID", "Publication Year", "Genre", "Pages", "Is Available", "Created At");

        System.out.println("----------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------");

        for (Book book : books) {
            System.out.printf("%-5s %-35s %-15s %-25s %-20s %-15s %-15s %-20s%n",
                    book.getId(), book.getTitle(), book.getAuthorId(), book.getPublicationYear(),
                    book.getGenre(), book.getPages(), book.getIsAvailable(), book.getCreatedAt());
        }
    }

}
