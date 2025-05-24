package main.java.dao.impl;

import com.sun.source.tree.BreakTree;
import main.java.dao.AuthorDao;
import main.java.model.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDAOImpl implements AuthorDao {

    private Connection conn;

    private static final String findAll = "SELECT * FROM authors";
    private static final String findById = "SELECT * FROM authors WHERE id = ?";
    private static final String save = "INSERT INTO authors (name, birth_year, nationality) VALUES(?, ?, ?)";
    private static final String deleteById = "DELETE FROM authors WHERE id = ?";
    private static final String update = "UPDATE authors SET name = ?, surname = ?, age = ? WHERE id = ?";

    public AuthorDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Author> findAll() throws SQLException {
        List<Author> AllAuthors = new ArrayList<>();
        try(Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(findAll)) {
                while (resultSet.next()){
                    Author author = new Author();
                    author.setId(resultSet.getInt("id"));
                    author.setName(resultSet.getString("name"));
                    author.setBirthYear(resultSet.getInt("birth_year"));
                    author.setNationality(resultSet.getString("nationality"));
                    AllAuthors.add(author);
                }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return AllAuthors;
    }

    @Override
    public Optional<Author> findById(int id) throws SQLException {

        Optional<Author> foundAuthor = null;
        try(PreparedStatement ps = conn.prepareStatement("findById")){
            ps.setInt(1,id);
            try(ResultSet resultSet = ps.executeQuery()){
                if(resultSet.next()) {
                    Author author = new Author();
                    author.setId(resultSet.getInt("id"));
                    author.setName(resultSet.getString("name"));
                    author.setBirthYear(resultSet.getInt("birth_year"));
                    author.setNationality(resultSet.getString("nationality"));

                    foundAuthor.of(author);
                    return foundAuthor;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Author save(Author author) throws SQLException {
        try(PreparedStatement preparedStatement = conn.prepareStatement(save, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getBirthYear());
            preparedStatement.setString(3, author.getNationality());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        author.setId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Author Created" + author.toString());
        }catch (SQLException e){
            e.printStackTrace();
        }
        return author;
    }

    @Override
    public void update(Author author) throws SQLException {
        try(PreparedStatement preparedStatement = conn.prepareStatement(update)){
            preparedStatement.setString(1, author.getName());
            preparedStatement.setInt(2, author.getBirthYear());
            preparedStatement.setString(3, author.getNationality());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Author Updated" + author.toString());
            }else{
                System.out.println("No Author with given ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        try(PreparedStatement preparedStatement = conn.prepareStatement(deleteById)){
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows > 0){
                System.out.println("Author Deleted");
            }else{
                System.out.println("No Author with given ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
