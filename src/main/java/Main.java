import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DatabaseConnection.getConnection()) {
            boolean isRunning = true;
            while (isRunning) {
                Menu.displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        insertLocationData(connection, scanner);
                        break;
                    case 2:
                        insertWeatherData(connection, scanner);
                        break;
                    case 3:
                        displayAllLocations(connection);
                        break;
                    case 4:
                        fetchWeatherData(connection, scanner);
                        break;
                    case 5:
                        isRunning = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void insertLocationData(Connection connection, Scanner scanner) {
        int id = UserInput.getIntInput(scanner, "Enter ID: ");
        String latitudeStr = UserInput.getLatitudeInput(scanner, "Enter rounded up latitude (e.g., -90 to 90): ");
        String longitudeStr = UserInput.getLongitudeInput(scanner, "Enter rounded up longitude (e.g., -180 to 180): ");
        String cityName = UserInput.getStringInput(scanner, "Enter City Name: ");
        String region = UserInput.getStringInput(scanner, "Enter Region: ");
        String countryName = UserInput.getStringInput(scanner, "Enter Country Name: ");
        LocationDataInsertion.insertLocationData(connection, id, latitudeStr, longitudeStr, cityName, region, countryName);
        System.out.println("Location data inserted successfully.");
    }
    private static void insertWeatherData(Connection connection, Scanner scanner) {
        int locationId = UserInput.getIntInput(scanner, "Enter Location ID: ");
        if (ValidationHelper.isIdValid(connection, locationId)) {
            scanner.nextLine();
            String date = UserInput.getDateInput(scanner, "Enter Date (e.g., 2020-01-01): ");
            String temperature = UserInput.getStringInput(scanner, "Enter Temperature in Celsius (e.g., 20.5): ");
            String humidity = UserInput.getStringInput(scanner, "Enter Humidity (e.g., 0.5): ");
            String pressure = UserInput.getStringInput(scanner, "Enter Pressure (e.g., 1000.0): ");
            WeatherDataInsertion.insertWeatherData(connection, locationId, date, temperature, pressure, humidity);
            System.out.println("Weather data inserted successfully.");
        } else {
            System.out.println("Invalid ID. Please enter a valid ID.");
        }
    }
    private static void displayAllLocations(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM locations";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                double latitude = resultSet.getDouble("Latitude");
                double longitude = resultSet.getDouble("Longitude");
                String cityName = resultSet.getString("CityName");
                String region = resultSet.getString("Region");
                String country = resultSet.getString("CountryName");

                System.out.println("ID: " + id);
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                System.out.println("City Name: " + cityName);
                System.out.println("Region: " + region);
                System.out.println("Country: " + country);
                System.out.println();
            }
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static String[] getLocationInfo(Connection connection, int locationID) {
        try {
            String query = "SELECT CityName, CountryName FROM locations WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, locationID);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String cityName = result.getString("CityName");
                String countryName = result.getString("CountryName");
                return new String[]{cityName, countryName};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{"", ""};
    }
    private static void fetchWeatherData(Connection connection, Scanner scanner) {
        System.out.println("Choose the input option:");
        System.out.println("1. Latitude and Longitude");
        System.out.println("2. City Name and Country");
        int inputChoice = scanner.nextInt();
        scanner.nextLine();

        String cityName = "";
        String countryName = "";

        if (inputChoice == 1) {
            System.out.print("Enter the rounded up Latitude(e.g, 34.56->35): ");
            double latitude = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter the rounded up Longitude(e.g, 114.66->114): ");
            double longitude = scanner.nextDouble();
            scanner.nextLine();

            int locationID = getLocationIDByGeoData(connection, latitude, longitude);

            if (locationID != -1) {
                String[] locationInfo = getLocationInfo(connection, locationID);
                cityName = locationInfo[0];
                countryName = locationInfo[1];

                System.out.print("Enter Date (e.g., 2020-01-01): ");
                String date = scanner.nextLine();
                fetchWeatherData(connection, locationID, date);
            } else {
                System.out.println("Location not found.");
            }
        } else if (inputChoice == 2) {
            System.out.print("Enter City Name: ");
            cityName = scanner.nextLine();
            System.out.print("Enter Country Name: ");
            countryName = scanner.nextLine();

            int locationID = getLocationIDByName(connection, cityName, countryName);

            if (locationID != -1) {
                System.out.print("Enter Date (e.g., 2020-01-01): ");
                String date = scanner.nextLine();
                fetchWeatherData(connection, locationID, date);
            } else {
                System.out.println("Location not found.");
            }
        } else {
            System.out.println("Invalid input option.");
        }

        if (!cityName.isEmpty() && !countryName.isEmpty()) {
            System.out.println("City: " + cityName);
            System.out.println("Country: " + countryName);
        }
    }
    private static int getLocationIDByGeoData(Connection connection, double latitude, double longitude) {
        try {
            String query = "SELECT ID FROM locations WHERE ROUND(Latitude, 0) = ? AND ROUND(Longitude, 0) = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, latitude);
            statement.setDouble(2, longitude);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private static int getLocationIDByName(Connection connection, String cityName, String countryName) {
        try {
            String query = "SELECT ID FROM locations WHERE CityName = ? AND CountryName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cityName);
            statement.setString(2, countryName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private static void fetchWeatherData(Connection connection, int locationID, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Date must be in the format yyyy-MM-dd.");
            return;
        }

        try {
            String query = "SELECT * FROM weatherdata WHERE LocationID = ? AND Date = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, locationID);
            statement.setString(2, date);
            ResultSet result = statement.executeQuery();

            boolean found = false;

            while (result.next()) {
                found = true;
                String dataDate = result.getString("Date");
                double temperature = result.getDouble("Temperature");
                double humidity = result.getDouble("Humidity");
                double pressure = result.getDouble("Pressure");
                System.out.println("Date: " + dataDate);
                System.out.println("Temperature: " + temperature +"C");
                System.out.println("Humidity: " + humidity);
                System.out.println("Pressure: " + pressure);
            }

            if (!found) {
                System.out.println("No weather data found for the specified date.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
