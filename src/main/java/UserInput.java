import java.util.Scanner;

public class UserInput {
    public static String getLatitudeInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        double latitude = scanner.nextDouble();
        scanner.nextLine();
        if (latitude >= -90 && latitude <= 90) {
            return (latitude >= 0) ? latitude + "°N" : -latitude + "°S";
        } else {
            System.out.println("Invalid latitude. Latitude must be between -90 and 90 degrees.");
            return getLatitudeInput(scanner, prompt);
        }
    }
    public static String getLongitudeInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        double longitude = scanner.nextDouble();
        scanner.nextLine();
        if (longitude >= -180 && longitude <= 180) {
            return (longitude >= 0) ? longitude + "°E" : -longitude + "°W";
        } else {
            System.out.println("Invalid longitude. Longitude must be between -180 and 180 degrees.");
            return getLongitudeInput(scanner, prompt);
        }
    }
    public static int getIntInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }
    public static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    public static String getDateInput(Scanner scanner, String prompt) {
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}";
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.matches(dateRegex)) {
                return input;
            } else {
                System.out.println("Invalid date format. Date must be in the format yyyy-MM-dd.");
            }
        }
    }
}

