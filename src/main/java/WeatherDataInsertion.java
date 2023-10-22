import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WeatherDataInsertion {
    private static final String INSERT_WEATHER_DATA_SQL = "INSERT INTO WeatherData (locationID, Date, Temperature, Pressure, Humidity) VALUES (?, ?, ?, ?, ?)";
    public static void insertWeatherData(Connection connection, int locationID, String date, String temperature, String pressure, String humidity) {
        try {
            String sql = "INSERT INTO weatherdata (locationID, Date, Temperature, Pressure, Humidity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, locationID);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, temperature);
            preparedStatement.setString(4, pressure);
            preparedStatement.setString(5, humidity);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}