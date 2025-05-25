package main.java.dao;
import main.java.model.Book;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookDao {
    List<Book> findBookByAuthor(String authorName) throws SQLException;
    List<Book> findAvailableBooks(int id) throws SQLException;
    Optional<Book> findById(int id) throws SQLException;
    Book save(Book book) throws SQLException;
    void update(Book book) throws SQLException;
    void deleteById(int id) throws SQLException;
    void borrowBook(int bookId) throws SQLException;
    void returnBook(int bookId) throws SQLException;
    Map<String, Long> getBookStatisticsByGenre() throws SQLException;
}