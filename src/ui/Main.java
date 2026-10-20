

package ui;

import repository.DatabaseSetup;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseSetup.createTables();

            LibraryUI libraryUI = new LibraryUI();
            libraryUI.start();

        } catch (SQLException e) {
            System.out.println("Could not initialize the database.");
            e.printStackTrace();
        }
    }
}
