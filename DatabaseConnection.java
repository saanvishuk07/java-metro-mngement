package metro;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/metro_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root"; // CHANGE THIS

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }
}
