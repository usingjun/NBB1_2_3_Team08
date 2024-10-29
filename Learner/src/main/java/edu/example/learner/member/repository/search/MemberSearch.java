package edu.example.learner.member.repository.search;

import edu.example.learner.member.dto.MemberDTO;
import edu.example.learner.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberSearch {
    Member getMemberInfo(Long id);

    List<Member> getAllMembers();

    Optional<Member> getMemberByEmail(String email);

    Optional<Member> getMemberByNickName(String nickName);
}
