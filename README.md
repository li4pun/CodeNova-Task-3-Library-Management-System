
# Library Management System

## Overview
A Java-based console application for managing library items, members, library cards, borrowing, returning, overdue status, and fine calculation. The application uses Object-Oriented Programming (OOP), JDBC, and SQLite for persistent data storage.

## Features
- Manage physical books and digital media.
- Register members with name and email validation.
- Issue library cards and prevent duplicate cards.
- Borrow and return items with inventory tracking.
- Detect duplicate catalog entries.
- Track active borrowings and overdue items.
- Calculate overdue fines at ₹5 per day.
- Store data persistently using SQLite.
- Handle unavailable items and invalid members using custom exceptions.

## Technologies
- Java 21
- SQLite
- JDBC
- SQLite JDBC Driver 3.53.4.0
- Java Collections Framework

## Project Structure
```text
Task-3-Library-Mangaement-System/
│
├── .gitignore
├── README.md
│
├── data/
│   └── library.db
│
├── lib/
│   └── sqlite-jdbc-3.53.4.0.jar
│
├── screenshots/
│
├── src/
│   ├── exception/
│   │   ├── BookNotAvailableException.java
│   │   └── MemberNotFoundException.java
│   │
│   ├── model/
│   │   ├── BorrowingRecord.java
│   │   ├── DigitalMedia.java
│   │   ├── LibraryCard.java
│   │   ├── LibraryItem.java
│   │   ├── Member.java
│   │   └── PhysicalBook.java
│   │
│   ├── repository/
│   │   ├── BorrowingRepository.java
│   │   ├── DatabaseConnection.java
│   │   ├── DatabaseSetup.java
│   │   ├── ItemRepository.java
│   │   ├── LibraryCardRepository.java
│   │   └── MemberRepository.java
│   │
│   ├── service/
│   │   ├── BorrowingService.java
│   │   ├── CatalogService.java
│   │   ├── LibraryCardService.java
│   │   └── MemberService.java
│   │
│   └── ui/
│       ├── LibraryUI.java
│       └── Main.java
│
└── out/
    └── Compiled Java classes
```
## Package Description
- **model:** Library items, members, cards, and borrowing records.
- **repository:** SQLite connectivity, schema creation, and data operations.
- **service:** Business rules, validation, borrowing, returning, and fine calculation.
- **ui:** Console menu and application entry point.
- **exception:** Custom exceptions for unavailable items and nonexistent members.

## Database Design
The SQLite database contains four tables:

- `items` — catalog information and inventory.
- `members` — registered member details.
- `library_cards` — member library cards.
- `borrowings` — borrowing dates, due dates, return dates, and fines.

Foreign keys maintain relationships between items, members, cards, and borrowing records.

## How to Run

**Prerequisite:** Install JDK 21 or a compatible JDK.

Compile from the project root:

```powershell
javac -cp "lib\sqlite-jdbc-3.53.4.0.jar" -d out src\model\*.java src\repository\*.java src\exception\*.java src\service\*.java src\ui\LibraryUI.java src\ui\Main.java
```

Run the application:

```````powershell
java -cp "out;lib\sqlite-jdbc-3.53.4.0.jar" ui.Main
```````

## Class Diagram Description

The main class relationships can be represented as follows:
                 LibraryItem
                 <<abstract>>
                 /          \
                /            \
       PhysicalBook        DigitalMedia


Main
 |
 v
LibraryUI
 |
 +-------------------+
 |         |         |
 v         v         v
Catalog   Member   Borrowing
Service   Service  Service
 |         |         |
 v         v         v
Item      Member   Borrowing
Repository Repository Repository


LibraryCardService
        |
        +--> LibraryCardRepository
        |
        +--> MemberRepository


DatabaseConnection
        |
        v
   SQLite Database
`LibraryItem` is an abstract class extended by `PhysicalBook` and `DigitalMedia`. The UI calls service classes, which apply business rules and use repository classes to interact with the SQLite database.

## Testing
The application has been tested for catalog operations, member registration, library-card issuance, borrowing, returning, inventory updates, overdue status, fine calculation, and invalid borrowing requests.

## Screenshots
Add operational screenshots to the `screenshots/` directory before submission.

## Conclusion
This project demonstrates Java OOP, inheritance, abstraction, polymorphism, JDBC, SQLite persistence, custom exceptions, and transaction-based inventory management.

