package model;

public class BorrowingRecord {
    private  int borrowingId;
    private int itemId;
    private int memberId;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private double fine;

    public BorrowingRecord(int borrowingId, int itemId,int memberId, String borrowDate,String dueDate,String returnDate,double fine)
    {
        this.borrowingId=borrowingId;
        this.itemId=itemId;
        this.memberId=memberId;
        this.borrowDate=borrowDate;
        this.dueDate=dueDate;
        this.returnDate=returnDate;
        this.fine=fine;
     }
     public int getItemId()
     {
        return itemId;
     }
    public int getMemberId()
    {
        return memberId;
    }
    public String getBorrowDate()
    {
        return borrowDate;
    }
    public String getDueDate()
    {
        return dueDate;
    }
    public String  getReturnDate()
    {
        return returnDate;

    }
  public double getFine()
  {
    return fine;
  }
  public boolean isOverdue(String currentDate)
  {
    return returnDate == null && currentDate.compareTo(dueDate)>0;
  }
public int getBorrowingId() {
    return borrowingId;
}
    
}
