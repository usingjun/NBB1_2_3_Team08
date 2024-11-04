package edu.example.learner_kotlin.qna.answer.service

import edu.example.learner_kotlin.qna.answer.dto.AnswerDTO
import edu.example.learner_kotlin.qna.answer.entity.Answer
import edu.example.learner_kotlin.qna.answer.repository.AnswerRepository
import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AnswerServiceImpl(private val answerRepository: AnswerRepository, private val modelMapper: ModelMapper) :
    AnswerService {
    override fun readByInquiryId(inquiryId: Long): AnswerDTO? = answerRepository.findByInquiryId(inquiryId)

    override fun readAll(): List<AnswerDTO> = answerRepository.readAll()

    override fun register(answerDTO: AnswerDTO): AnswerDTO =
        AnswerDTO(answerRepository.save(Answer().apply {
            answerContent = answerDTO.answerContent
            inquiry = Inquiry().apply { inquiryId = answerDTO.inquiryId }
        }))

    @Transactional
    override fun update(answerDTO: AnswerDTO): AnswerDTO = run {
        val answer =
            answerRepository.findByIdOrNull(answerDTO.answerId) ?: throw NoSuchElementException("Answer not found")
        with(answer) {
            answerContent = answerDTO.answerContent
        }
        AnswerDTO(answer)
    }

    override fun delete(answerId: Long) = answerRepository.delete(
        answerRepository.findByIdOrNull(answerId) ?: throw NoSuchElementException("Answer $answerId not found")
    )

    override fun deleteByInquiryId(inquiryId: Long) = answerRepository.deleteByInquiryId(inquiryId)

}