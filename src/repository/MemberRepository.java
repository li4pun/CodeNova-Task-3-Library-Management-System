
package repository;

import model.Member;
import java.sql.*;
import java.util.*;

public class MemberRepository {

    public void addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            statement.executeUpdate();
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY member_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                members.add(new Member(
                    resultSet.getInt("member_id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
                ));
            }
        }

        return members;
    }

    public int findMemberIdByEmail(String email) throws SQLException {
        String sql = "SELECT member_id FROM members WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("member_id");
                }
            }
        }

        return -1;
    }
    
public int findMemberIdByEmailForId(int memberId) throws SQLException {
    String sql = "SELECT member_id FROM members WHERE member_id = ?";

    try (var connection = DatabaseConnection.getConnection();
         var statement = connection.prepareStatement(sql)) {

        statement.setInt(1, memberId);

        try (var result = statement.executeQuery()) {
            if (result.next()) {
                return result.getInt("member_id");
            }
        }
    }

    return -1;
}

public void deleteMember(int memberId) throws SQLException {
    String checkSql = """
        SELECT COUNT(*)
        FROM borrowings
        WHERE member_id = ?
        """;

    try (var connection = DatabaseConnection.getConnection();
         var check = connection.prepareStatement(checkSql)) {

        check.setInt(1, memberId);

        try (var result = check.executeQuery()) {
            if (result.next() && result.getInt(1) > 0) {
                throw new SQLException(
                    "Cannot delete this member because borrowing records exist."
                );
            }
        }

        String cardSql = "DELETE FROM library_cards WHERE member_id = ?";
        try (var statement = connection.prepareStatement(cardSql)) {
            statement.setInt(1, memberId);
            statement.executeUpdate();
        }

        String memberSql = "DELETE FROM members WHERE member_id = ?";
        try (var statement = connection.prepareStatement(memberSql)) {
            statement.setInt(1, memberId);

            if (statement.executeUpdate() == 0) {
                throw new SQLException("Member ID not found.");
            }
        }
    }
}

}
