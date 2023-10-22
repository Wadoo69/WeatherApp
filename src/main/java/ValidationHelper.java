import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidationHelper {
    public static boolean isIdValid(Connection connection, int locationID) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM locations WHERE ID = ?");
            statement.setInt(1, locationID);
            ResultSet result = statement.executeQuery();
            if (result.next() && result.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}