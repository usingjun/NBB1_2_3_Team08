package edu.example.learner_kotlin.qna.faq.repository

import edu.example.learner_kotlin.qna.faq.dto.FAQDTO
import edu.example.learner_kotlin.qna.faq.entity.FAQ
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FAQRepository : JpaRepository<FAQ, Long> {
    @Query("select new edu.example.learner_kotlin.qna.faq.dto.FAQDTO(f) from FAQ f where f.faqCategory = :faqCategory")
    fun findByFaqCategory(faqCategory: FAQCategory): List<FAQDTO>

    @Query("select new edu.example.learner_kotlin.qna.faq.dto.FAQDTO(f) from FAQ f")
    fun getAllDTO(): List<FAQDTO>
}