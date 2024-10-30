package edu.example.learner_kotlin.qna.answer.repository

import edu.example.learner_kotlin.qna.answer.dto.AnswerDTO
import edu.example.learner_kotlin.qna.answer.entity.Answer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface AnswerRepository : JpaRepository<Answer, Long> {
    @Query("select new edu.example.learner_kotlin.qna.answer.dto.AnswerDTO(a) from Answer a where a.inquiry.inquiryId = :inquiryId")
    fun findByInquiryId(@Param("inquiryId") inquiryId: Long): AnswerDTO?

    @Query("select new edu.example.learner_kotlin.qna.answer.dto.AnswerDTO(a) from Answer a")
    fun readAll(): List<AnswerDTO>

    @Modifying
    @Transactional
    @Query("delete from Answer a where a.inquiry.inquiryId = :inquiryId")
    fun deleteByInquiryId(@Param("inquiryId") inquiryId: Long)
}