package model;

public class PhysicalBook extends LibraryItem {
    public PhysicalBook(int itemId, String title,int totalCopies,int availableCopies)
    {
        super(itemId,title,totalCopies,availableCopies);

    }
@Override 
public String getItemType()
{
    return "PHYSICAL_BOOK";
}

}
