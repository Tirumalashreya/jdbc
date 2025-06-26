package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cabdb";
        String user = "root";      // or "shreya" if that's your MySQL user
        String pass = "shreya";    // your actual password

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected successfully to MySQL!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
