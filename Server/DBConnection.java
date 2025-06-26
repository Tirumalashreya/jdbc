//DBConnection
package Server;

import java.sql.*;
import javax.sql.DataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/cabdb";
    private static final String USER = "root";
    private static final String PASSWORD = "shreya";
    
    private static MysqlDataSource dataSource;
    
    static {
        try {
            dataSource = new MysqlDataSource();
            dataSource.setURL(URL);
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);
            dataSource.setConnectTimeout(5000);
            dataSource.setSocketTimeout(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}

