

package ui;

import model.LibraryItem;
import model.PhysicalBook;
import model.DigitalMedia;
import model.Member;
import model.LibraryCard;
import model.BorrowingRecord;
import repository.ItemRepository;
import repository.MemberRepository;
import repository.BorrowingRepository;
import service.CatalogService;
import service.MemberService;
import service.LibraryCardService;
import service.BorrowingService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LibraryUI {

    private final Scanner scanner = new Scanner(System.in);
    private final CatalogService catalogService = new CatalogService();
    private final MemberService memberService = new MemberService();
    private final LibraryCardService cardService = new LibraryCardService();
    private final BorrowingService borrowingService = new BorrowingService();

    public void start() {
        while (true) {
            System.out.println("\n===Welcome to  Library Management System ===");
            System.out.println("1. Add Physical Book");
            System.out.println("2. Add Digital Media");
            System.out.println("3. View catalog");
            System.out.println("4.Delete book/item");
            System.out.println("5. Register Member");
            System.out.println("6. View Members");
            System.out.println("7.Delete Member");
            System.out.println("8. Issue Library Card");
            System.out.println("9. Borrow Item");
            System.out.println("10. Return Item");
            System.out.println("11. View Active Borrowings");
            System.out.println("12. Exit Application.");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> addItem(true);
                    case "2" -> addItem(false);
                    case "3" -> viewCatalog();
                    case "4" -> deleteItem();
                    case "5" ->registerMember();                   
                     case "6" -> viewMembers();
                    case "7"->deleteMember();          
                 case "8" -> issueCard();
                    case "9" -> borrowItem();
                    case "10" -> returnItem();
                    case "11" -> viewActiveBorrowings();
                    case "12" -> {
                        System.out.println("Exiting library system.");
                        return;
                    }
                    default -> System.out.println("Invalid menu option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    private void addItem(boolean physical) throws SQLException {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Total copies: ");
        int copies = Integer.parseInt(scanner.nextLine());

        LibraryItem item = physical
            ? new PhysicalBook(0, title, copies, copies)
            : new DigitalMedia(0, title, copies, copies);
       
List<LibraryItem> duplicates = catalogService.addItem(item);

if (duplicates.isEmpty()) {
    System.out.println("Item added successfully.");
    return;
}

System.out.println("WARNING: This item already exists in the catalog.");

for (LibraryItem duplicate : duplicates) {
    System.out.printf(
        "ID: %d | Title: %s | Type: %s | Available: %d/%d%n",
        duplicate.getItemId(),
        duplicate.getTitle(),
        duplicate.getItemType(),
        duplicate.getAvailableCopies(),
        duplicate.getTotalCopies()
    );
}

System.out.print("Do you want to delete an existing entry? (Y/N): ");
String answer = scanner.nextLine().trim();

if (answer.equalsIgnoreCase("Y")) {
    System.out.print("Enter the ID of the entry to delete: ");
    int itemId = Integer.parseInt(scanner.nextLine());

    boolean validId = duplicates.stream()
        .anyMatch(existing -> existing.getItemId() == itemId);

    if (!validId) {
        System.out.println("Invalid ID. No entry was deleted.");
        return;
    }

    catalogService.deleteItem(itemId);
    System.out.println("Entry deleted successfully.");
} else {
    System.out.println("Existing entry kept. No entry was deleted.");
}

        
    }

    private void viewCatalog() throws SQLException {
        List<LibraryItem> items = catalogService.getAllItems();

        for (LibraryItem item : items) {
            System.out.printf(
                "ID: %d | Title: %s | Type: %s | Available: %d/%d%n",
                item.getItemId(),
                item.getTitle(),
                item.getItemType(),
                item.getAvailableCopies(),
                item.getTotalCopies()
            );
        }

        if (items.isEmpty()) {
            System.out.println("Catalog is empty.");
        }
    }
    
private void registerMember() throws SQLException {
    while (true) {
        System.out.print("Member name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        try {
            memberService.registerMember(new Member(0, name, email));
            System.out.println("Member registered successfully.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("Please enter the details again.\n");
        }
    }
}

    

    private void viewMembers() throws SQLException {
        List<Member> members = memberService.getAllMembers();

        for (Member member : members) {
            System.out.printf(
                "ID: %d | Name: %s | Email: %s%n",
                member.getMemberId(),
                member.getName(),
                member.getEmail()
            );
        }

        if (members.isEmpty()) {
            System.out.println("No members registered.");
        }
    }

    private void issueCard() throws SQLException {
        System.out.print("Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine());

        cardService.issueCard(memberId);
        System.out.println("Library card issued.");
    }

    private void borrowItem() throws Exception {
        System.out.print("Item ID: ");
        int itemId = Integer.parseInt(scanner.nextLine());

        System.out.print("Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine());

        borrowingService.borrowItem(itemId, memberId);
        System.out.println("Item borrowed successfully.");
    }

    private void returnItem() throws Exception {
        System.out.print("Borrowing ID: ");
        int borrowingId = Integer.parseInt(scanner.nextLine());

        borrowingService.returnItem(borrowingId);
    }
   
private void viewActiveBorrowings() throws SQLException {
    BorrowingRepository repository = new BorrowingRepository();
    List<BorrowingRecord> records = repository.getActiveBorrowings();

    String currentDate = java.time.LocalDate.now().toString();

    for (BorrowingRecord record : records) {
        String status = record.isOverdue(currentDate)
                ? "OVERDUE"
                : "ACTIVE";

        System.out.printf(
            "Borrowing ID: %d | Item ID: %d | Member ID: %d | Due: %s | Status: %s%n",
            record.getBorrowingId(),
            record.getItemId(),
            record.getMemberId(),
            record.getDueDate(),
            status
        );
    }

    if (records.isEmpty()) {
        System.out.println("No active borrowings.");
    }
}

    

private void deleteItem() throws SQLException {
    System.out.print("Enter item ID to delete: ");
    int itemId = Integer.parseInt(scanner.nextLine());

    catalogService.deleteItem(itemId);
    System.out.println("Item deleted successfully.");
}

private void deleteMember() throws SQLException {
    viewMembers();

    System.out.print("Enter member ID to delete: ");
    int memberId = Integer.parseInt(scanner.nextLine());

    System.out.print(
        "Are you sure you want to delete this member? (Y/N): "
    );
    String answer = scanner.nextLine().trim();

    if (!answer.equalsIgnoreCase("Y")) {
        System.out.println("Member deletion cancelled.");
        return;
    }

    memberService.deleteMember(memberId);
    System.out.println("Member deleted successfully.");
}


}
