package model;

public class DigitalMedia extends LibraryItem{

    public DigitalMedia(int itemId,String title,int totalCopies,int availableCopies)
    {
        super(itemId,title,totalCopies,availableCopies);

    }
    

    public String getItemType()
    {
        return "DIGITAL_MEDIA";
    }
}
