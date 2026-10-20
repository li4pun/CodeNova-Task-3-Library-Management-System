package model;

public abstract class LibraryItem {


    private int itemId;
    private String title;
    private int totalCopies;
    private int availableCopies;

    public LibraryItem(int itemId,String title,int totalCopies,int availableCopies)
    {
        this.itemId=itemId;
        this.title=title;
        this.totalCopies=totalCopies;
        this.availableCopies=availableCopies;

    }
    public int getItemId()
    {
        return itemId;
    }
   public String getTitle()
   {
    return title;
   }
    public int getTotalCopies()
    {
        return totalCopies;
    }
    public int getAvailableCopies()
    {
        return availableCopies;
    }
    public void setAvailableCopies(int availableCopies)
    {
        if(availableCopies<0||availableCopies>totalCopies)
        {
            throw new IllegalArgumentException("Available copies must be between 0 and total copies.");

        }
        this.availableCopies=availableCopies;
    }
    public abstract String getItemType();
}
