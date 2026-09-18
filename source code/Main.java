package metro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line entry point for the Metro Management System.
 *
 * This class intentionally uses only standard Java console I/O so the project
 * can be evaluated from a terminal without any GUI setup.
 */
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final MetroService SERVICE = new MetroService();

    public static void main(String[] args) {
        printBanner();

        if (args.length > 0 && "--help".equalsIgnoreCase(args[0])) {
            printUsage();
            return;
        }

        if (!testDatabaseConnection()) {
            System.out.println("\nDatabase connection failed.");
            System.out.println("Check DatabaseConnection.java, MySQL status, and the JDBC driver.");
            System.out.println("See README.md for the exact command-line setup.\n");
            return;
        }

        mainMenu();
        System.out.println("Thank you for using Metro Management System!");
    }

    private static void printBanner() {
        System.out.println("===============================================");
        System.out.println("          METRO MANAGEMENT SYSTEM");
        System.out.println("        Smart Card Integration - CLI");
        System.out.println("===============================================");
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -cp <classpath> metro.Main");
        System.out.println("  java -cp <classpath> metro.Main --help");
        System.out.println();
        System.out.println("The application is fully terminal-based; no GUI is required.");
    }

    private static boolean testDatabaseConnection() {
        try (Connection ignored = DatabaseConnection.getConnection()) {
            return true;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n--------------- MAIN MENU ----------------");
            System.out.println("1. Passenger Login");
            System.out.println("2. Register Passenger");
            System.out.println("3. View Stations");
            System.out.println("4. Fare Information");
            System.out.println("5. Demo Login");
            System.out.println("0. Exit");
            System.out.println("-------------------------------------------");

            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> login();
                    case 2 -> register();
                    case 3 -> viewStations();
                    case 4 -> fareInformation();
                    case 5 -> demoLogin();
                    case 0 -> { return; }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private static void register() throws Exception {
        System.out.println("\n========== PASSENGER REGISTRATION ==========");
        String name = readRequired("Name: ");
        String phone = readRequired("Phone: ");
        String email = readLine("Email (optional): ");
        String password = readRequired("Password: ");

        Passenger passenger = SERVICE.registerPassenger(name, phone, email, password);
        System.out.println("Registration successful!");
        System.out.println("Passenger ID: " + passenger.getId());
        System.out.println("Please use your phone and password to log in.");
    }

    private static void login() throws Exception {
        System.out.println("\n=============== PASSENGER LOGIN ============");
        String phone = readRequired("Phone: ");
        String password = readRequired("Password: ");

        Passenger passenger = SERVICE.loginPassenger(phone, password);
        if (passenger == null) {
            System.out.println("Invalid phone number or password.");
            return;
        }

        passengerMenu(passenger);
    }

    private static void demoLogin() throws Exception {
        Passenger passenger = SERVICE.loginPassenger("9999999999", "demo123");
        if (passenger == null) {
            System.out.println("Demo passenger was not found. Run sql/metro_system.sql first.");
            return;
        }
        System.out.println("Demo login successful.");
        passengerMenu(passenger);
    }

    private static void passengerMenu(Passenger passenger) {
        while (true) {
            System.out.println("\n=============================================");
            System.out.println("Welcome, " + passenger.getName());
            System.out.println("=============================================");
            System.out.println("1. View Smart Card");
            System.out.println("2. Issue Smart Card");
            System.out.println("3. Recharge Smart Card");
            System.out.println("4. Start Journey (Entry)");
            System.out.println("5. End Journey (Exit)");
            System.out.println("6. View Stations");
            System.out.println("7. View Journey History");
            System.out.println("8. View Fare Information");
            System.out.println("9. Logout");
            System.out.println("---------------------------------------------");

            int choice = readInt("Enter choice: ");
            try {
                switch (choice) {
                    case 1 -> viewCard(passenger);
                    case 2 -> issueCard(passenger);
                    case 3 -> rechargeCard(passenger);
                    case 4 -> startJourney(passenger);
                    case 5 -> endJourney(passenger);
                    case 6 -> viewStations();
                    case 7 -> viewHistory(passenger);
                    case 8 -> fareInformation();
                    case 9 -> { System.out.println("Logged out successfully."); return; }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                printError(e);
            }
        }
    }

    private static void viewCard(Passenger passenger) throws SQLException {
        SmartCard card = SERVICE.getCard(passenger.getId());
        if (card == null) {
            System.out.println("No active smart card found.");
            return;
        }

        System.out.println("\n--------------- SMART CARD -----------------");
        System.out.println("Card ID     : " + card.getCardId());
        System.out.println("Card Number : " + card.getCardNumber());
        System.out.printf("Balance     : ₹%.2f%n", card.getBalance());
        System.out.println("Status      : " + card.getStatus());
    }

    private static void issueCard(Passenger passenger) throws Exception {
        SmartCard existing = SERVICE.getCard(passenger.getId());
        if (existing != null) {
            System.out.println("You already have an active card: " + existing.getCardNumber());
            return;
        }

        System.out.println("Standard initial balance is ₹150.");
        double amount = readDouble("Enter initial balance (minimum ₹50): ");
        if (amount < 50) {
            System.out.println("Initial balance must be at least ₹50.");
            return;
        }

        SmartCard card = SERVICE.issueCard(passenger.getId(), amount);
        System.out.println("Smart card issued successfully!");
        System.out.println("Card Number: " + card.getCardNumber());
        System.out.printf("Balance: ₹%.2f%n", card.getBalance());
    }

    private static void rechargeCard(Passenger passenger) throws Exception {
        SmartCard card = requireCard(passenger);
        if (card == null) return;

        double amount = readDouble("Enter recharge amount: ");
        SERVICE.recharge(card.getCardId(), amount);
        SmartCard updated = SERVICE.getCard(passenger.getId());
        System.out.printf("Recharge successful. New balance: ₹%.2f%n", updated.getBalance());
    }

    private static void startJourney(Passenger passenger) throws Exception {
        SmartCard card = requireCard(passenger);
        if (card == null) return;

        List<Station> stations = SERVICE.getStations();
        printStations(stations);
        int stationId = readInt("Enter entry station ID: ");

        SERVICE.startJourney(card.getCardId(), stationId);
        System.out.println("Entry recorded successfully.");
        System.out.println("Remember to end the journey at your destination station.");
    }

    private static void endJourney(Passenger passenger) throws Exception {
        SmartCard card = requireCard(passenger);
        if (card == null) return;

        List<Station> stations = SERVICE.getStations();
        printStations(stations);
        int stationId = readInt("Enter exit station ID: ");

        double fare = SERVICE.endJourney(card.getCardId(), stationId);
        SmartCard updated = SERVICE.getCard(passenger.getId());
        System.out.println("Journey completed successfully.");
        System.out.printf("Fare deducted : ₹%.2f%n", fare);
        System.out.printf("New balance   : ₹%.2f%n", updated.getBalance());
    }

    private static SmartCard requireCard(Passenger passenger) throws SQLException {
        SmartCard card = SERVICE.getCard(passenger.getId());
        if (card == null) {
            System.out.println("No active smart card found. Issue a card first.");
        }
        return card;
    }

    private static void viewStations() throws SQLException {
        System.out.println("\n--------------- METRO STATIONS ------------");
        printStations(SERVICE.getStations());
    }

    private static void printStations(List<Station> stations) {
        if (stations.isEmpty()) {
            System.out.println("No stations available.");
            return;
        }

        System.out.printf("%-5s %-22s %-15s %-10s%n", "ID", "Station", "Line", "Distance");
        System.out.println("------------------------------------------------------------");
        for (Station station : stations) {
            System.out.printf("%-5d %-22s %-15s %-9.2f km%n",
                    station.getStationId(), station.getStationName(),
                    station.getLineName(), station.getDistanceFromOrigin());
        }
    }

    private static void fareInformation() {
        System.out.println("\n--------------- FARE INFORMATION -----------");
        System.out.println("Distance              Fare");
        System.out.println("0 - 5 km              ₹10");
        System.out.println("Above 5 - 10 km       ₹20");
        System.out.println("Above 10 - 20 km      ₹30");
        System.out.println("Above 20 km           ₹40");
    }

    private static void viewHistory(Passenger passenger) throws SQLException {
        String sql = "SELECT j.entry_time, s1.station_name AS entry_s, " +
                "s2.station_name AS exit_s, j.fare, j.status " +
                "FROM journeys j " +
                "JOIN smart_cards c ON j.card_id=c.card_id " +
                "JOIN stations s1 ON j.entry_station_id=s1.station_id " +
                "LEFT JOIN stations s2 ON j.exit_station_id=s2.station_id " +
                "WHERE c.passenger_id=? ORDER BY j.entry_time DESC";

        System.out.println("\n---------------- JOURNEY HISTORY -----------");
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, passenger.getId());
            try (ResultSet rs = statement.executeQuery()) {
                boolean found = false;
                System.out.printf("%-20s %-20s %-20s %-10s %-12s%n",
                        "Entry Time", "Entry", "Exit", "Fare", "Status");
                System.out.println("--------------------------------------------------------------------------------");
                while (rs.next()) {
                    found = true;
                    String exit = rs.getString("exit_s");
                    if (exit == null) exit = "-";
                    System.out.printf("%-20s %-20s %-20s ₹%-9.2f %-12s%n",
                            rs.getTimestamp("entry_time"),
                            rs.getString("entry_s"), exit,
                            rs.getDouble("fare"), rs.getString("status"));
                }
                if (!found) System.out.println("No journeys found.");
            }
        }
    }

    private static String readRequired(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isBlank()) return value;
            System.out.println("This field is required.");
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void printError(Exception e) {
        System.out.println("Operation failed: " + e.getMessage());
    }
}
