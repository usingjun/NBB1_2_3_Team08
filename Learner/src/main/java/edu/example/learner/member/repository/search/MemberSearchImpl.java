package edu.example.learner.member.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import edu.example.learner.member.dto.MemberDTO;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.QMember;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MemberSearchImpl implements MemberSearch{
    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;


    @Override
    public Member getMemberInfo(Long id) {
        return queryFactory
                .selectFrom(qMember)
                .where(qMember.memberId.eq(id))
                .fetchOne();
    }

    @Override
    public List<Member> getAllMembers() {
        return queryFactory
                .selectFrom(qMember)
                .fetch();
    }

    @Override
    public Optional<Member> getMemberByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(email))
                .fetchOne());
    }

    @Override
    public Optional<Member> getMemberByNickName(String nickName) {
        return Optional.ofNullable(queryFactory
                .selectFrom(qMember)
                .where(qMember.nickname.eq(nickName))
                .fetchOne());
    }

}
