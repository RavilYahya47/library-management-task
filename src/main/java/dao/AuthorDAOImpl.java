package main.java.dao;

import main.java.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    private static AuthorDAOImpl dao;
    private final Connection connection;

    public static AuthorDAOImpl of(Connection connection){
        if(dao == null){
            dao = new AuthorDAOImpl(connection);
        }

        return dao;
    }

    private AuthorDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Author> findAll() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from authors");
        List<Author> authors = new ArrayList<>();
        while (rs.next()) {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            author.setBirthYear(rs.getInt("birth_year"));
            author.setNationality(rs.getString("nationality"));
            authors.add(author);
        }
        return authors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("select * from authors where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return Optional.of(getAuthorFromResultSet(rs));
        }

        return Optional.empty();
    }

    private Author getAuthorFromResultSet(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getInt("id"));
        author.setName(rs.getString("name"));
        author.setBirthYear(rs.getInt("birth_year"));
        author.setNationality(rs.getString("nationality"));

        return author;
    }

    @Override
    public Author save(Author author) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("insert into authors(name, birth_year, nationality) values(?,?,?)");
        stmt.setString(1, author.getName());
        stmt.setInt(2, author.getBirthYear());
        stmt.setString(3, author.getNationality());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Author could not be inserted into authors");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating author failed, no ID obtained.");
            }
        }

        return author;
    }

    @Override
    public void update(Author author) throws SQLException {
        if (author.getId() == null) {
            throw new SQLException("Author id is null");
        }

        PreparedStatement stmt = connection.prepareStatement("update authors(name, birth_year, nationality) set values(?,?,?) where id = ?");
        stmt.setString(1, author.getName());
        stmt.setInt(2, author.getBirthYear());
        stmt.setString(3, author.getNationality());
        stmt.setInt(2, author.getId());

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("No rows affected");
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("delete from authors where id = ?");
        stmt.setInt(1, id);

        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("No rows affected");
        }
    }
}
