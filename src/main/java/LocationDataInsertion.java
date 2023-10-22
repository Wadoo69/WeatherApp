import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationDataInsertion {
    public static void insertLocationData(Connection connection, int id, String latitudeStr, String longitudeStr, String cityName, String region, String countryName) {
        if (latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
            System.out.println("Latitude and longitude cannot be empty.");
            return;
        }
        String insertSQL = "INSERT INTO Locations (ID, Latitude, Longitude, CityName, Region, CountryName) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            double latitudeValue = parseLatitude(latitudeStr);
            double longitudeValue = parseLongitude(longitudeStr);
            preparedStatement.setInt(1, id);
            preparedStatement.setDouble(2, latitudeValue);
            preparedStatement.setDouble(3, longitudeValue);
            preparedStatement.setString(4, cityName);
            preparedStatement.setString(5, region);
            preparedStatement.setString(6, countryName);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Location data inserted successfully.");
            } else {
                System.out.println("Location data not inserted. Please check your input.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid latitude or longitude format.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static double parseLatitude(String latitude) {
        double parsedValue = 0.0;
        try {
            String[] parts = latitude.split("°");
            double value = Double.parseDouble(parts[0]);

            if (latitude.toUpperCase().endsWith("S")) {
                value = -value;
            }
            parsedValue = value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parsedValue;
    }

    private static double parseLongitude(String longitude) {
        double parsedValue = 0.0;
        try {
            String[] parts = longitude.split("°");
            double value = Double.parseDouble(parts[0]);

            if (longitude.toUpperCase().endsWith("W")) {
                value = -value;
            }
            parsedValue = value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parsedValue;
    }
}