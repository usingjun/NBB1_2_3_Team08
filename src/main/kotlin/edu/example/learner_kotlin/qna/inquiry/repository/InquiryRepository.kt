package edu.example.learner_kotlin.qna.inquiry.repository

import edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO
import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface InquiryRepository : JpaRepository<Inquiry, Long> {
    @Query("select new edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO(i) from Inquiry i where i.member.memberId = :memberId")
    fun findByMemberId(@Param("memberId") memberId: Long): List<InquiryDTO>
}