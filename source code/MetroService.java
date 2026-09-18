package metro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MetroService {

    public Passenger registerPassenger(String name, String phone, String email, String password)
            throws Exception {
        if (name == null || name.isBlank() || phone == null || phone.isBlank() ||
                password == null || password.isBlank()) {
            throw new ValidationException("Name, phone and password are required.");
        }
        if (!phone.matches("\\d{10,15}")) {
            throw new ValidationException("Phone number must contain 10 to 15 digits.");
        }
        if (password.length() < 4) {
            throw new ValidationException("Password must contain at least 4 characters.");
        }

        String sql = "INSERT INTO passengers(name,phone,email,password) VALUES(?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name.trim());
            ps.setString(2, phone.trim());
            ps.setString(3, email == null || email.isBlank() ? null : email.trim());
            ps.setString(4, password);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("Passenger could not be created.");
                return new Passenger(rs.getInt(1), name.trim(), phone.trim(), email);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ValidationException("Phone number is already registered.");
        }
    }

    public Passenger loginPassenger(String phone, String password) throws SQLException {
        String sql = "SELECT * FROM passengers WHERE phone=? AND password=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Passenger(rs.getInt("passenger_id"), rs.getString("name"),
                            rs.getString("phone"), rs.getString("email"));
                }
                return null;
            }
        }
    }

    public SmartCard issueCard(int passengerId, double initialBalance) throws Exception {
        if (initialBalance < 0) {
            throw new ValidationException("Initial balance cannot be negative.");
        }

        String existingSql = "SELECT card_id FROM smart_cards WHERE passenger_id=? AND status='ACTIVE' LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement check = c.prepareStatement(existingSql)) {
            check.setInt(1, passengerId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) throw new ValidationException("Passenger already has an active smart card.");
            }
        }

        String number = "METRO" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sql = "INSERT INTO smart_cards(card_number,passenger_id,balance) VALUES(?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, number);
            ps.setInt(2, passengerId);
            ps.setDouble(3, initialBalance);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new SQLException("Smart card could not be created.");
                return new SmartCard(rs.getInt(1), number, initialBalance, "ACTIVE");
            }
        }
    }

    public SmartCard getCard(int passengerId) throws SQLException {
        String sql = "SELECT * FROM smart_cards WHERE passenger_id=? AND status='ACTIVE' LIMIT 1";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, passengerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SmartCard(rs.getInt("card_id"), rs.getString("card_number"),
                            rs.getDouble("balance"), rs.getString("status"));
                }
                return null;
            }
        }
    }

    public void recharge(int cardId, double amount) throws Exception {
        if (amount <= 0) throw new ValidationException("Recharge must be greater than zero.");

        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p1 = c.prepareStatement(
                    "UPDATE smart_cards SET balance=balance+? WHERE card_id=? AND status='ACTIVE'")) {
                p1.setDouble(1, amount);
                p1.setInt(2, cardId);
                if (p1.executeUpdate() == 0) {
                    throw new ValidationException("Card is invalid or blocked.");
                }
            }

            try (PreparedStatement p2 = c.prepareStatement(
                    "INSERT INTO transactions(card_id,transaction_type,amount,description) VALUES(?,'RECHARGE',?,?)")) {
                p2.setInt(1, cardId);
                p2.setDouble(2, amount);
                p2.setString(3, "Smart card recharge");
                p2.executeUpdate();
            }
            c.commit();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Station> getStations() throws SQLException {
        List<Station> list = new ArrayList<>();
        String sql = "SELECT * FROM stations ORDER BY distance_from_origin";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Station(rs.getInt("station_id"), rs.getString("station_name"),
                        rs.getString("line_name"), rs.getDouble("distance_from_origin")));
            }
        }
        return list;
    }

    public void startJourney(int cardId, int stationId) throws Exception {
        SmartCard card = getCardById(cardId);
        if (card == null || !card.isActive()) {
            throw new ValidationException("Card is blocked/invalid.");
        }
        if (!stationExists(stationId)) {
            throw new ValidationException("Entry station not found.");
        }

        String check = "SELECT journey_id FROM journeys WHERE card_id=? AND status='OPEN'";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(check)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) throw new ValidationException("Journey already in progress.");
            }
        }

        String sql = "INSERT INTO journeys(card_id,entry_station_id) VALUES(?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setInt(2, stationId);
            ps.executeUpdate();
        }
    }

    public double endJourney(int cardId, int exitStationId) throws Exception {
        if (!stationExists(exitStationId)) {
            throw new ValidationException("Exit station not found.");
        }

        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int journeyId;
                double entryDistance;

                String find = "SELECT j.journey_id,s.distance_from_origin FROM journeys j " +
                        "JOIN stations s ON j.entry_station_id=s.station_id " +
                        "WHERE j.card_id=? AND j.status='OPEN' FOR UPDATE";
                try (PreparedStatement ps = c.prepareStatement(find)) {
                    ps.setInt(1, cardId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new ValidationException("No open journey found.");
                        journeyId = rs.getInt("journey_id");
                        entryDistance = rs.getDouble("distance_from_origin");
                    }
                }

                double exitDistance;
                try (PreparedStatement exitPs = c.prepareStatement(
                        "SELECT distance_from_origin FROM stations WHERE station_id=?")) {
                    exitPs.setInt(1, exitStationId);
                    try (ResultSet exitRs = exitPs.executeQuery()) {
                        if (!exitRs.next()) throw new ValidationException("Exit station not found.");
                        exitDistance = exitRs.getDouble(1);
                    }
                }

                double distance = Math.abs(exitDistance - entryDistance);
                double fare = FareCalculator.calculate(distance);

                double balance;
                try (PreparedStatement balancePs = c.prepareStatement(
                        "SELECT balance,status FROM smart_cards WHERE card_id=? FOR UPDATE")) {
                    balancePs.setInt(1, cardId);
                    try (ResultSet balanceRs = balancePs.executeQuery()) {
                        if (!balanceRs.next() || !"ACTIVE".equals(balanceRs.getString("status"))) {
                            throw new ValidationException("Card is blocked/invalid.");
                        }
                        balance = balanceRs.getDouble("balance");
                    }
                }

                if (balance < fare) {
                    throw new ValidationException("Insufficient balance. Required: ₹" + fare);
                }

                try (PreparedStatement p1 = c.prepareStatement(
                        "UPDATE smart_cards SET balance=balance-? WHERE card_id=?")) {
                    p1.setDouble(1, fare);
                    p1.setInt(2, cardId);
                    p1.executeUpdate();
                }

                try (PreparedStatement p2 = c.prepareStatement(
                        "UPDATE journeys SET exit_station_id=?,exit_time=NOW(),fare=?,status='COMPLETED' WHERE journey_id=?")) {
                    p2.setInt(1, exitStationId);
                    p2.setDouble(2, fare);
                    p2.setInt(3, journeyId);
                    p2.executeUpdate();
                }

                try (PreparedStatement p3 = c.prepareStatement(
                        "INSERT INTO transactions(card_id,transaction_type,amount,description) VALUES(?,'FARE',?,?)")) {
                    p3.setInt(1, cardId);
                    p3.setDouble(2, fare);
                    p3.setString(3, "Metro journey fare");
                    p3.executeUpdate();
                }

                c.commit();
                return fare;
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        }
    }

    private boolean stationExists(int stationId) throws SQLException {
        String sql = "SELECT station_id FROM stations WHERE station_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, stationId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private SmartCard getCardById(int cardId) throws SQLException {
        String sql = "SELECT * FROM smart_cards WHERE card_id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SmartCard(rs.getInt("card_id"), rs.getString("card_number"),
                            rs.getDouble("balance"), rs.getString("status"));
                }
                return null;
            }
        }
    }
}
