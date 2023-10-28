import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DatabaseConnection.getConnection()) {
            WeatherDatabaseController wc = new WeatherDatabaseController(connection);

            boolean isRunning = true;
            while (isRunning) {
                Menu.displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        wc.insertLocationData(scanner);
                        break;
                    case 2:
                        wc.insertWeatherData(scanner);
                        break;
                    case 3:
                        wc.displayAllLocations();
                        break;
                    case 4:
                        wc.fetchWeatherData(scanner);
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
}
