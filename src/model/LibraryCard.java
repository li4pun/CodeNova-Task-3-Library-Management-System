package model;

public class LibraryCard {

private int cardId;
private int memberId;
private String issueDate;

public LibraryCard(int cardId,int memberId,String issueDate)
{
    this.cardId=cardId;
    this.memberId=memberId;
    this.issueDate=issueDate;
}
public int getCardId()
{
    return  cardId;
}
public int getMemberId()
{
    return memberId;
}
public String getIssueDate()
{
    return issueDate;
}
    
}
