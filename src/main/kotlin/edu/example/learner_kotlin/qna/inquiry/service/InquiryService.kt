package edu.example.learner_kotlin.qna.inquiry.service

import edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO

interface InquiryService {
    fun read(inquiryId: Long): InquiryDTO
    fun readByMemberId(memberId: Long): List<InquiryDTO>
    fun readAll(): List<InquiryDTO>
    fun register(inquiryDTO: InquiryDTO): InquiryDTO
    fun update(inquiryDTO: InquiryDTO): InquiryDTO
    fun delete(inquiryId: Long)
}