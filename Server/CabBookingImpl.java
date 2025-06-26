package Server;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CabBookingImpl extends UnicastRemoteObject implements CabBookingInterface {
    
    public CabBookingImpl() throws RemoteException {
        super();
    }

    @Override
    public String bookCab(String name, String source, String destination) throws RemoteException {
        if (name == null || name.trim().isEmpty() || 
            source == null || source.trim().isEmpty() || 
            destination == null || destination.trim().isEmpty()) {
            return "Error: All fields are required!";
        }
        
        try (Connection conn = DBConnection.getConnection()) {
           
            String checkCabQuery = "SELECT id, driver_name FROM cabs WHERE location = ? AND is_available = 1 LIMIT 1";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkCabQuery)) {
                checkStmt.setString(1, source.trim());
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    int cabId = rs.getInt("id");
                    String driverName = rs.getString("driver_name");
                    
                   
                    conn.setAutoCommit(false);
                    
                    try {
                        
                        String insertBooking = "INSERT INTO bookings (name, source, destination, cab_id, booking_time, status) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pst = conn.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS)) {
                            pst.setString(1, name.trim());
                            pst.setString(2, source.trim());
                            pst.setString(3, destination.trim());
                            pst.setInt(4, cabId);
                            pst.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                            pst.setString(6, "CONFIRMED");
                            pst.executeUpdate();
                            
                            ResultSet generatedKeys = pst.getGeneratedKeys();
                            int bookingId = 0;
                            if (generatedKeys.next()) {
                                bookingId = generatedKeys.getInt(1);
                            }
                            
                            // Update cab availability
                            String updateCab = "UPDATE cabs SET is_available = 0, current_location = ? WHERE id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateCab)) {
                                updateStmt.setString(1, destination.trim());
                                updateStmt.setInt(2, cabId);
                                updateStmt.executeUpdate();
                            }
                            
                            conn.commit();
                            
                            return String.format(" Cab Booked Successfully!\n" +
                                               "Booking ID: %d\n" +
                                               "Cab ID: %d\n" +
                                               "Driver: %s\n" +
                                               "Route: %s → %s", 
                                               bookingId, cabId, driverName, source, destination);
                        }
                    } catch (SQLException e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                    }
                } else {
                    return "❌ No available cabs at " + source + ". Try a different location.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Booking failed: " + e.getMessage();
        }
    }

    @Override
    public List<String> getAvailableCabs(String location) throws RemoteException {
        List<String> availableCabs = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, driver_name, cab_type FROM cabs WHERE location = ? AND is_available = 1";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, location);
                ResultSet rs = pst.executeQuery();
                
                while (rs.next()) {
                    String cabInfo = String.format("Cab ID: %d, Driver: %s, Type: %s", 
                                                   rs.getInt("id"), 
                                                   rs.getString("driver_name"),
                                                   rs.getString("cab_type"));
                    availableCabs.add(cabInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            availableCabs.add("Error fetching available cabs: " + e.getMessage());
        }
        
        return availableCabs;
    }

    @Override
    public String cancelBooking(int bookingId) throws RemoteException {
        try (Connection conn = DBConnection.getConnection()) {
         
            conn.setAutoCommit(false);
            
            try {
                
                String getBookingQuery = "SELECT cab_id, status FROM bookings WHERE id = ?";
                try (PreparedStatement getStmt = conn.prepareStatement(getBookingQuery)) {
                    getStmt.setInt(1, bookingId);
                    ResultSet rs = getStmt.executeQuery();
                    
                    if (rs.next()) {
                        String status = rs.getString("status");
                        if ("CANCELLED".equals(status)) {
                            return " Booking is already cancelled!";
                        }
                        
                        int cabId = rs.getInt("cab_id");
                        
                     
                        String updateBooking = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateBooking)) {
                            updateStmt.setInt(1, bookingId);
                            updateStmt.executeUpdate();
                        }
                        
                        
                        String updateCab = "UPDATE cabs SET is_available = 1 WHERE id = ?";
                        try (PreparedStatement updateCabStmt = conn.prepareStatement(updateCab)) {
                            updateCabStmt.setInt(1, cabId);
                            updateCabStmt.executeUpdate();
                        }
                        
                        conn.commit();
                        return " Booking cancelled successfully! Cab ID " + cabId + " is now available.";
                    } else {
                        return " Booking not found!";
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return " Error cancelling booking: " + e.getMessage();
        }
    }

    @Override
    public String getBookingHistory(String customerName) throws RemoteException {
        StringBuilder history = new StringBuilder();
        history.append("📋 Booking History for ").append(customerName).append(":\n\n");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, source, destination, cab_id, booking_time, status FROM bookings WHERE name = ? ORDER BY booking_time DESC";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, customerName);
                ResultSet rs = pst.executeQuery();
                
                int count = 0;
                while (rs.next()) {
                    count++;
                    history.append(String.format("%d. Booking ID: %d\n", count, rs.getInt("id")))
                           .append(String.format("   Route: %s → %s\n", rs.getString("source"), rs.getString("destination")))
                           .append(String.format("   Cab ID: %d\n", rs.getInt("cab_id")))
                           .append(String.format("   Time: %s\n", rs.getTimestamp("booking_time")))
                           .append(String.format("   Status: %s\n\n", rs.getString("status")));
                }
                
                if (count == 0) {
                    history.append("No bookings found for this customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error fetching booking history: " + e.getMessage();
        }
        
        return history.toString();
    }

    @Override
    public String addCab(String location) throws RemoteException {
        try (Connection conn = DBConnection.getConnection()) {
            String insertCab = "INSERT INTO cabs (location, is_available, driver_name, cab_type) VALUES (?, 1, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(insertCab, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, location);
                pst.setString(2, "Driver_" + System.currentTimeMillis() % 1000); // Auto-generate driver name
                pst.setString(3, "Sedan"); // Default cab type
                pst.executeUpdate();
                
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int cabId = generatedKeys.getInt(1);
                    return "New cab added successfully! Cab ID: " + cabId + " at " + location;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error adding cab: " + e.getMessage();
        }
        
        return "❌ Failed to add cab";
    }

    @Override
    public String getCabStatus() throws RemoteException {
        StringBuilder status = new StringBuilder();
        status.append("🚕 Cab Fleet Status:\n\n");
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, location, current_location, is_available, driver_name, cab_type FROM cabs ORDER BY id";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                ResultSet rs = pst.executeQuery();
                
                int available = 0, busy = 0;
                while (rs.next()) {
                    boolean isAvailable = rs.getBoolean("is_available");
                    if (isAvailable) available++;
                    else busy++;
                    
                    status.append(String.format("Cab ID: %d | %s | Driver: %s | Type: %s\n", 
                                                rs.getInt("id"),
                                                isAvailable ? "🟢 Available at " + rs.getString("location") : 
                                                            "🔴 Busy (at " + rs.getString("current_location") + ")",
                                                rs.getString("driver_name"),
                                                rs.getString("cab_type")));
                }
                
                status.append(String.format("\n📊 Summary: %d Available, %d Busy", available, busy));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error fetching cab status: " + e.getMessage();
        }
        
        return status.toString();
    }
}

