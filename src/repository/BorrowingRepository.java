

package repository;

import model.BorrowingRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository {

    public void addBorrowing(BorrowingRecord record)
            throws SQLException {

        String sql = """
            INSERT INTO borrowings
            (item_id, member_id, borrow_date, due_date, return_date, fine)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.getItemId());
            statement.setInt(2, record.getMemberId());
            statement.setString(3, record.getBorrowDate());
            statement.setString(4, record.getDueDate());
            statement.setString(5, record.getReturnDate());
            statement.setDouble(6, record.getFine());

            statement.executeUpdate();
        }
    }

    public List<BorrowingRecord> getActiveBorrowings()
            throws SQLException {

        List<BorrowingRecord> records = new ArrayList<>();

        String sql = """
            SELECT * FROM borrowings
            WHERE return_date IS NULL
            ORDER BY borrowing_id
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                records.add(new BorrowingRecord(
                    resultSet.getInt("borrowing_id"),
                    resultSet.getInt("item_id"),
                    resultSet.getInt("member_id"),
                    resultSet.getString("borrow_date"),
                    resultSet.getString("due_date"),
                    resultSet.getString("return_date"),
                    resultSet.getDouble("fine")
                ));
            }
        }

        return records;
    }
    
public void markAsReturned(int borrowingId, String returnDate, double fine)
        throws SQLException {

    String sql = """
        UPDATE borrowings
        SET return_date = ?, fine = ?
        WHERE borrowing_id = ? AND return_date IS NULL
        """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, returnDate);
        statement.setDouble(2, fine);
        statement.setInt(3, borrowingId);

        int updatedRows = statement.executeUpdate();

        if (updatedRows == 0) {
            throw new SQLException("Active borrowing record not found.");
        }
    }
}

public void addBorrowing(Connection connection, BorrowingRecord record)
        throws SQLException {

    String sql = """
        INSERT INTO borrowings
        (item_id, member_id, borrow_date, due_date, return_date, fine)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, record.getItemId());
        statement.setInt(2, record.getMemberId());
        statement.setString(3, record.getBorrowDate());
        statement.setString(4, record.getDueDate());
        statement.setString(5, record.getReturnDate());
        statement.setDouble(6, record.getFine());
        statement.executeUpdate();
    }
}

public BorrowingRecord findActiveBorrowing(
        Connection connection, int borrowingId) throws SQLException {

    String sql = """
        SELECT borrowing_id, item_id, member_id,
               borrow_date, due_date, return_date, fine
        FROM borrowings
        WHERE borrowing_id = ? AND return_date IS NULL
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, borrowingId);

        try (var result = statement.executeQuery()) {
            if (result.next()) {
                return new BorrowingRecord(
                    result.getInt("borrowing_id"),
                    result.getInt("item_id"),
                    result.getInt("member_id"),
                    result.getString("borrow_date"),
                    result.getString("due_date"),
                    result.getString("return_date"),
                    result.getDouble("fine")
                );
            }
        }
    }

    return null;
}

public void markAsReturned(
        Connection connection,
        int borrowingId,
        String returnDate,
        double fine) throws SQLException {

    String sql = """
        UPDATE borrowings
        SET return_date = ?, fine = ?
        WHERE borrowing_id = ? AND return_date IS NULL
        """;

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, returnDate);
        statement.setDouble(2, fine);
        statement.setInt(3, borrowingId);

        if (statement.executeUpdate() == 0) {
            throw new SQLException("Active borrowing record not found.");
        }
    }
}


}
