
package service;
import repository.BorrowingRepository;
import repository.DatabaseConnection;
import java.sql.Connection;

import model.BorrowingRecord;
import model.DigitalMedia;
import model.LibraryItem;
import model.PhysicalBook;
import repository.ItemRepository;
import repository.MemberRepository;
import repository.BorrowingRepository;
import exception.BookNotAvailableException;
import exception.MemberNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowingService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final BorrowingRepository borrowingRepository;
    private static final double FINE_PER_DAY = 5.0;

    public BorrowingService() {
        this.itemRepository = new ItemRepository();
       this.borrowingRepository = new BorrowingRepository();
        this.memberRepository = new MemberRepository();
    }

    public void validateBorrowing(
            LibraryItem item, int memberId)
            throws SQLException, BookNotAvailableException,
                   MemberNotFoundException {

        if (memberRepository.findMemberIdByEmailForId(memberId) == -1) {
            throw new MemberNotFoundException("Member does not exist.");
        }

        if (item == null || item.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(
                "This item is currently unavailable."
            );
        }
    }

    public double calculateFine(String dueDate, String returnDate) {
        LocalDate due = LocalDate.parse(dueDate);
        LocalDate returned = LocalDate.parse(returnDate);

        long overdueDays = ChronoUnit.DAYS.between(due, returned);

        return Math.max(0, overdueDays) * FINE_PER_DAY;
    }

public void borrowItem(int itemId, int memberId)
        throws Exception {

    try (Connection connection = DatabaseConnection.getConnection()) {
        connection.setAutoCommit(false);

        try {
            LibraryItem item = null;

            String findItemSql = """
                SELECT item_id, title, item_type,
                       total_copies, available_copies
                FROM items
                WHERE item_id = ?
                """;

            try (var statement = connection.prepareStatement(findItemSql)) {
                statement.setInt(1, itemId);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("item_id");
                        String title = resultSet.getString("title");
                        String type = resultSet.getString("item_type");
                        int total = resultSet.getInt("total_copies");
                        int available = resultSet.getInt("available_copies");

                        if ("PHYSICAL_BOOK".equals(type)) {
                            item = new PhysicalBook(id, title, total, available);
                        } else if ("DIGITAL_MEDIA".equals(type)) {
                            item = new DigitalMedia(id, title, total, available);
                        }
                    }
                }
            }

            validateBorrowing(item, memberId);

            String updateSql = """
                UPDATE items
                SET available_copies = available_copies - 1
                WHERE item_id = ? AND available_copies > 0
                """;

            try (var statement = connection.prepareStatement(updateSql)) {
                statement.setInt(1, itemId);

                if (statement.executeUpdate() == 0) {
                    throw new BookNotAvailableException(
                        "Item does not exist or no copies are available."
                    );
                }
            }

            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(14);

            BorrowingRecord record = new BorrowingRecord(
                0, itemId, memberId,
                borrowDate.toString(), dueDate.toString(),
                null, 0.0
            );

            borrowingRepository.addBorrowing(connection, record);
            connection.commit();

            System.out.println("Item borrowed successfully.");

        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}


            
public void returnItem(int borrowingId) throws Exception {
    try (Connection connection = DatabaseConnection.getConnection()) {
        connection.setAutoCommit(false);

        try {
            BorrowingRecord record =
                borrowingRepository.findActiveBorrowing(connection, borrowingId);

            if (record == null) {
                throw new IllegalArgumentException(
                    "Active borrowing record not found."
                );
            }

            LocalDate returnDate = LocalDate.now();

            double fine = calculateFine(
                record.getDueDate(),
                returnDate.toString()
            );

            String sql = """
                UPDATE items
                SET available_copies = available_copies + 1
                WHERE item_id = ? AND available_copies < total_copies
                """;

            try (var statement = connection.prepareStatement(sql)) {
                statement.setInt(1, record.getItemId());

                if (statement.executeUpdate() == 0) {
                    throw new SQLException(
                        "Inventory could not be updated for this item."
                    );
                }
            }

            borrowingRepository.markAsReturned(
                connection,
                borrowingId,
                returnDate.toString(),
                fine
            );

            connection.commit();

            System.out.println("Item returned successfully.");
            System.out.println("Fine: Rs. " + fine);

        } catch (Exception e) {
            connection.rollback();
            throw e;
        }
    }
}


}
