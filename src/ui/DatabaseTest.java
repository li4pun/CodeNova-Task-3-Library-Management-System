
    
    
package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:data/library.db";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("SQLite database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}


