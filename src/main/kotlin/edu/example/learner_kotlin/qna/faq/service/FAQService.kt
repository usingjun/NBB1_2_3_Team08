package edu.example.learner_kotlin.qna.faq.service

import edu.example.learner_kotlin.qna.faq.dto.FAQDTO
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory

interface FAQService {
    fun read(faqId: Long): FAQDTO
    fun readAll(): List<FAQDTO>
    fun readByCategory(faqCategory: FAQCategory): List<FAQDTO>
    fun register(faqDTO: FAQDTO): FAQDTO
    fun update(faqDTO: FAQDTO): FAQDTO
    fun delete(faqId: Long)
}