import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "1234";

    private static final Connection connection;
    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Verilənlər bazasına bağlantı alınmadı: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
