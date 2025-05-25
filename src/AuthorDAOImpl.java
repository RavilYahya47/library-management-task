import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class AuthorDAOImpl implements AuthorDAO {
    private final Connection connection = DatabaseConnection.getConnection();
    public List<Author> findAll() throws SQLException {
        String sql = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Author author = mapResultSetToAuthor(resultSet);
                authors.add(author);
            }
        }
        return authors;
    }
    public Optional<Author> findById(int id) throws SQLException {
        String sql = "SELECT * FROM authors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToAuthor(resultSet));
                }
            }
        }
        return Optional.empty();
    }
    public Author save(Author author) throws SQLException {
        String sql = "INSERT INTO authors(id,name,birthYear,nationality) VALUES(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, author.getId());
            statement.setString(2, author.getName());
            statement.setInt(3, author.getBirthYear());
            statement.setString(4, author.getNationality());
            statement.executeUpdate();
            int rowsAffected = statement.getUpdateCount();
            if (rowsAffected>0) {
                return author;
            }
            else throw new RuntimeException("Muellif elave olunmadi");
        }
    }
    public void update(Author author) throws SQLException {
        String sql = "UPDATE  authors SET id = ?,name =?,birthYear =?,nationality =?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, author.getId());
            statement.setString(2, author.getName());
            statement.setInt(3, author.getBirthYear());
            statement.setString(4, author.getNationality());
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Müəllif uğurla yeniləndi.");
            } else {
                throw new RuntimeException("Update olunmadi");
            }
        }

}
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE * FROM authors WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Muellif ugurla silindi");
            }
            else throw new AuthorNotFoundException("Bele bir muellif tapilmadi");
        }
        catch (AuthorNotFoundException e){
            System.out.println(e.getMessage());
        }
    }
    private Author mapResultSetToAuthor(ResultSet resultSet) {
            try {
                Author author = new Author();
                author.setId(resultSet.getInt("id"));
                author.setName(resultSet.getString("name"));
                author.setNationality(resultSet.getString("nationality"));
                author.setBirthYear(resultSet.getInt("birthYear"));
                return author;
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }

    }