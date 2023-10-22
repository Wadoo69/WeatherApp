import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost/WeatherApp";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}