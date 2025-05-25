package main.java.dao;

import main.java.model.Author;
import main.java.model.Book;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDAOImpl implements BookDAO {
    private static BookDAOImpl dao;
    private final Connection connection;

    public static BookDAOImpl of(Connection connection){
        if(dao == null){
            dao = new BookDAOImpl(connection);
        }

        return dao;
    }

    private BookDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from books");
        List<Book> books = new ArrayList<>();
        while (rs.next()) {
            Book book = getBookFromResultSet(rs);
            books.add(book);
        }
        return books;
    }

    @Override
    public Optional<Book> findById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from books where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return Optional.of(getBookFromResultSet(rs));
        }

        return Optional.empty();
    }

    private Book getBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(rs.getInt("author_id"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setGenre(rs.getString("genre"));
        book.setPages(rs.getInt("pages"));
        book.setAvailable(rs.getBoolean("available"));
        String createdAt = rs.getString("created_at");
        LocalDateTime created = LocalDateTime.parse(createdAt);
        book.setCreatedAt(created);

        return book;
    }

    @Override
    public Book save(Book book) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("insert into books(title, author_id, publication_year, genre, pages) values(?,?,?,?,?)");
        stmt.setString(1, book.getTitle());
        stmt.setInt(2, book.getAuthorId());
        stmt.setInt(3, book.getPublicationYear());
        stmt.setString(4, book.getGenre());
        stmt.setInt(5, book.getPages());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Book could not be inserted into books");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating author failed, no ID obtained.");
            }
        }

        return book;
    }

    @Override
    public void update(Book book) throws SQLException {
        if (book.getId() == null) {
            throw new SQLException("Book id is null");
        }

        PreparedStatement stmt = connection.prepareStatement("update books(title, author_id, publication_year, genre, pages) set values(?,?,?,?,?) where id = ?");
        stmt.setString(1, book.getTitle());
        stmt.setInt(2, book.getAuthorId());
        stmt.setInt(3, book.getPublicationYear());
        stmt.setString(4, book.getGenre());
        stmt.setInt(5, book.getPages());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("No rows affected");
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("delete from books where id = ?");
        stmt.setInt(1, id);

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("No rows affected");
        }
    }
}
