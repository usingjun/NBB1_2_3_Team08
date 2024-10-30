package edu.example.learner_kotlin.member.repository

import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun getMemberByNickname(memberNickname: String): Member?
}