
package service;

import model.Member;
import repository.MemberRepository;

import java.sql.SQLException;
import java.util.List;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService() {
        this.memberRepository = new MemberRepository();
    }
    
public void registerMember(Member member) throws SQLException {
    if (member == null) {
        throw new IllegalArgumentException("Member cannot be null.");
    }

    if (member.getName() == null ||
            !member.getName().matches("[\\p{L}][\\p{L} .'-]*")) {
        throw new IllegalArgumentException(
            "Enter a valid name using letters and normal name punctuation."
        );
    }

    if (member.getEmail() == null ||
            !member.getEmail().matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
        throw new IllegalArgumentException("Enter a valid email address.");
    }

    if (memberRepository.findMemberIdByEmail(member.getEmail()) != -1) {
        throw new IllegalArgumentException(
            "A member with this email already exists."
        );
    }

    memberRepository.addMember(member);
}

    

    public List<Member> getAllMembers() throws SQLException {
        return memberRepository.getAllMembers();
    }
    
public void deleteMember(int memberId) throws SQLException {
    if (memberId <= 0) {
        throw new IllegalArgumentException("Invalid member ID.");
    }

    memberRepository.deleteMember(memberId);
}

}
