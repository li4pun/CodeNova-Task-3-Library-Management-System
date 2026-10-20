

package repository;

import model.LibraryCard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryCardRepository {

    public void issueCard(LibraryCard card) throws SQLException {
        String sql = """
            INSERT INTO library_cards (member_id, issue_date)
            VALUES (?, ?)
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, card.getMemberId());
            statement.setString(2, card.getIssueDate());
            statement.executeUpdate();
        }
    }

    public LibraryCard findCardByMemberId(int memberId)
            throws SQLException {

        String sql = """
            SELECT card_id, member_id, issue_date
            FROM library_cards
            WHERE member_id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new LibraryCard(
                        resultSet.getInt("card_id"),
                        resultSet.getInt("member_id"),
                        resultSet.getString("issue_date")
                    );
                }
            }
        }

        return null;
    }
}
