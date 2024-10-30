package edu.example.learner_kotlin.qna.answer.service

import edu.example.learner_kotlin.qna.answer.dto.AnswerDTO

interface AnswerService {
    fun readByInquiryId(inquiryId: Long): AnswerDTO
    fun readAll(): List<AnswerDTO>
    fun register(answerDTO: AnswerDTO): AnswerDTO
    fun update(answerDTO: AnswerDTO): AnswerDTO
    fun delete(answerId: Long)
    fun deleteByInquiryId(inquiryId: Long)
}