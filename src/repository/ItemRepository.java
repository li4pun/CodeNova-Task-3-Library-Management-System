

package repository;


import model.*;
import java.sql.*;
import java.util.*;


public class ItemRepository {

    public void addItem(LibraryItem item) throws SQLException {
        String sql = """
            INSERT INTO items
            (title, item_type, total_copies, available_copies)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, item.getTitle());
            statement.setString(2, item.getItemType());
            statement.setInt(3, item.getTotalCopies());
            statement.setInt(4, item.getAvailableCopies());

            statement.executeUpdate();
        }
    }

    public List<LibraryItem> getAllItems() throws SQLException {
        List<LibraryItem> items = new ArrayList<>();

        String sql = "SELECT * FROM items ORDER BY item_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String title = resultSet.getString("title");
                String type = resultSet.getString("item_type");
                int total = resultSet.getInt("total_copies");
                int available = resultSet.getInt("available_copies");

                if ("PHYSICAL_BOOK".equals(type)) {
                    items.add(new PhysicalBook(id, title, total, available));
                } else if ("DIGITAL_MEDIA".equals(type)) {
                    items.add(new DigitalMedia(id, title, total, available));
                }
            }
        }

        return items;
    }
    
public void decreaseAvailableCopies(int itemId) throws SQLException {
    String sql = """
        UPDATE items
        SET available_copies = available_copies - 1
        WHERE item_id = ? AND available_copies > 0
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, itemId);

        if (statement.executeUpdate() == 0) {
            throw new SQLException("No copies available for this item.");
        }
    }
}

public void increaseAvailableCopies(int itemId) throws SQLException {
    String sql = """
        UPDATE items
        SET available_copies = available_copies + 1
        WHERE item_id = ? AND available_copies < total_copies
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, itemId);

        if (statement.executeUpdate() == 0) {
            throw new SQLException(
                "Inventory could not be increased. Check the item ID and inventory."
            );
        }
    }
}

public void deleteItem(int itemId) throws SQLException {
    String checkSql = """
        SELECT COUNT(*)
        FROM borrowings
        WHERE item_id = ?
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement check = connection.prepareStatement(checkSql)) {

        check.setInt(1, itemId);

        try (var result = check.executeQuery()) {
            if (result.next() && result.getInt(1) > 0) {
                throw new SQLException(
                    "Cannot delete this item because borrowing records exist."
                );
            }
        }

        String deleteSql = "DELETE FROM items WHERE item_id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(deleteSql)) {

            statement.setInt(1, itemId);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Item ID not found.");
            }
        }
    }
}
public List<LibraryItem> findDuplicates(String title, String itemType)
        throws SQLException {

    List<LibraryItem> duplicates = new ArrayList<>();

    String sql = """
        SELECT * FROM items
        WHERE LOWER(TRIM(title)) = LOWER(TRIM(?))
          AND item_type = ?
        ORDER BY item_id
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, title);
        statement.setString(2, itemType);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("item_id");
                String existingTitle = resultSet.getString("title");
                int total = resultSet.getInt("total_copies");
                int available = resultSet.getInt("available_copies");

                if ("PHYSICAL_BOOK".equals(itemType)) {
                    duplicates.add(
                        new PhysicalBook(id, existingTitle, total, available)
                    );
                } else {
                    duplicates.add(
                        new DigitalMedia(id, existingTitle, total, available)
                    );
                }
            }
        }
    }

    return duplicates;
}


}
