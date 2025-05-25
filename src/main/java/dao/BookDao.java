package main.java.dao;
import main.java.model.Book;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BookDao {
    List<Book> findBookByAuthor(String authorName) throws SQLException;
    List<Book> findAvailableBooks(int id) throws SQLException;
    public void borrowBook(int bookId) throws SQLException;
    public void returnBook(int bookId) throws SQLException;
    public Map<String, Long> getBookStatisticsByGenre() throws SQLException;
}
