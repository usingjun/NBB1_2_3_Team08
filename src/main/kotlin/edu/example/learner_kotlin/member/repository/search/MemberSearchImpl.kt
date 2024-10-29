package edu.example.learner_kotlin.member.repository.search

import com.querydsl.jpa.impl.JPAQueryFactory
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.QMember


class MemberSearchImpl(
    private val queryFactory: JPAQueryFactory,
    private val qMember: QMember = QMember.member
) : MemberSearch {

    override fun getMemberInfo(id: Long?): Member? {
        return queryFactory
            .selectFrom(qMember)
            .where(qMember.memberId.eq(id))
            .fetchOne()
    }

    override val allMembers: List<Member>
        get() = queryFactory
            .selectFrom(qMember)
            .fetch()

    override fun getMemberByEmail(email: String?): Member? {
        return queryFactory
            .selectFrom(qMember)
            .where(qMember.email.eq(email))
            .fetchOne()
    }

    override fun getMemberByNickName(nickName: String?): Member? {
        return queryFactory
            .selectFrom(qMember)
            .where(qMember.nickname.eq(nickName))
            .fetchOne()
    }
}