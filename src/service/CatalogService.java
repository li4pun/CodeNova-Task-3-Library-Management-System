
package service;

import model.LibraryItem;
import repository.ItemRepository;

import java.sql.SQLException;
import java.util.List;

public class CatalogService {

    private final ItemRepository itemRepository;

    public CatalogService() {
        this.itemRepository = new ItemRepository();
    }
    
public List<LibraryItem> addItem(LibraryItem item) throws SQLException {
    if (item == null) {
        throw new IllegalArgumentException("Library item cannot be null.");
    }

    if (item.getTitle() == null || item.getTitle().isBlank()) {
        throw new IllegalArgumentException("Item title cannot be empty.");
    }

    if (item.getTotalCopies() <= 0) {
        throw new IllegalArgumentException("Total copies must be greater than zero.");
    }

    List<LibraryItem> duplicates =
        itemRepository.findDuplicates(item.getTitle(), item.getItemType());

    if (!duplicates.isEmpty()) {
        return duplicates;
    }

    itemRepository.addItem(item);
    return List.of();
}

    

    public List<LibraryItem> getAllItems() throws SQLException {
        return itemRepository.getAllItems();
    }
    
public void deleteItem(int itemId) throws SQLException {
    if (itemId <= 0) {
        throw new IllegalArgumentException("Invalid item ID.");
    }

    itemRepository.deleteItem(itemId);
}

}
