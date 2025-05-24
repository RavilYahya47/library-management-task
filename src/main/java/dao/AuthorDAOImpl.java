package main.java.dao;

import main.java.model.Author;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDAO {

    private final Connection connection;

    public AuthorDAOImpl(Connection connection) {
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
        return Optional.empty();
    }

    @Override
    public Author save(Author author) throws SQLException {
        return null;
    }

    @Override
    public void update(Author author) throws SQLException {

    }

    @Override
    public void deleteById(int id) throws SQLException {

    }
}
