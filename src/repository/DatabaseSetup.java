
package repository;

import java.sql.*;

public class DatabaseSetup {

    

    public static void createTables() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
     Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS items (
                    item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    item_type TEXT NOT NULL
                        CHECK (item_type IN ('PHYSICAL_BOOK', 'DIGITAL_MEDIA')),
                    total_copies INTEGER NOT NULL CHECK (total_copies >= 0),
                    available_copies INTEGER NOT NULL
                        CHECK (available_copies >= 0
                               AND available_copies <= total_copies)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS members (
                    member_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS library_cards (
                    card_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    member_id INTEGER NOT NULL UNIQUE,
                    issue_date TEXT NOT NULL,
                    FOREIGN KEY (member_id) REFERENCES members(member_id)
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS borrowings (
                    borrowing_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    item_id INTEGER NOT NULL,
                    member_id INTEGER NOT NULL,
                    borrow_date TEXT NOT NULL,
                    due_date TEXT NOT NULL,
                    return_date TEXT,
                    fine REAL NOT NULL DEFAULT 0 CHECK (fine >= 0),
                    FOREIGN KEY (item_id) REFERENCES items(item_id),
                    FOREIGN KEY (member_id) REFERENCES members(member_id)
                )
                """);

            System.out.println("All database tables created successfully!");
        }
    }

    public static void main(String[] args) {
        try {
            createTables();
        } catch (SQLException e) {
            System.out.println("Database setup failed: " + e.getMessage());
        }
    }
}

