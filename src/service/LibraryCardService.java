

package service;

import model.LibraryCard;
import repository.LibraryCardRepository;
import repository.MemberRepository;

import java.sql.SQLException;
import java.time.LocalDate;

public class LibraryCardService {

    private final LibraryCardRepository cardRepository;
    private final MemberRepository memberRepository;

    public LibraryCardService() {
        this.cardRepository = new LibraryCardRepository();
        this.memberRepository = new MemberRepository();
    }

    public void issueCard(int memberId) throws SQLException {
        if (memberRepository.findMemberIdByEmailForId(memberId) == -1) {
            throw new IllegalArgumentException("Member does not exist.");
        }

        if (cardRepository.findCardByMemberId(memberId) != null) {
            throw new IllegalArgumentException("Member already has a library card.");
        }

        LibraryCard card = new LibraryCard(
            0,
            memberId,
            LocalDate.now().toString()
        );

        cardRepository.issueCard(card);
    }
}
