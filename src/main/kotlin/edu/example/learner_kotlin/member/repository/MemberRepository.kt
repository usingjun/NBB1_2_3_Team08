package edu.example.learner_kotlin.member.repository

import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByEmail(email: String?): Member

    fun findByPhoneNumber(phoneNumber: String?): List<Member?>

    fun existsByEmail(email: String?): Boolean
}