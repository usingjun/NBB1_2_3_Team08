package edu.example.learner_kotlin.qna.inquiry.service

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO
import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import edu.example.learner_kotlin.qna.inquiry.entity.InquiryStatus
import edu.example.learner_kotlin.qna.inquiry.repository.InquiryRepository
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InquiryServiceImpl(private val inquiryRepository: InquiryRepository, private val modelMapper: ModelMapper) :
    InquiryService {
    override fun read(inquiryId: Long): InquiryDTO = InquiryDTO(
        inquiryRepository.findByIdOrNull(inquiryId) ?: throw NoSuchElementException("Inquiry $inquiryId not found")
    )

    override fun readByMemberId(memberId: Long): List<InquiryDTO> = inquiryRepository.findByMemberId(memberId)

    override fun readAll(): List<InquiryDTO> = inquiryRepository.findAll().map { InquiryDTO(it) }

    override fun register(inquiryDTO: InquiryDTO): InquiryDTO =
        InquiryDTO(inquiryRepository.save(Inquiry().apply {
            inquiryTitle = inquiryDTO.inquiryTitle
            inquiryContent = inquiryDTO.inquiryContent
            inquiryStatus = InquiryStatus.valueOf(inquiryDTO.inquiryStatus!!)
            member = Member().apply {
                memberId = inquiryDTO.memberId
                nickname = inquiryDTO.memberNickname
            }
        }))

    override fun update(inquiryDTO: InquiryDTO): InquiryDTO = run {
        val inquiry =
            inquiryRepository.findByIdOrNull(inquiryDTO.inquiryId) ?: throw NoSuchElementException("Inquiry not found")
        with(inquiry) {
            inquiryTitle = inquiryDTO.inquiryTitle
            inquiryContent = inquiryDTO.inquiryContent
            inquiryStatus = InquiryStatus.valueOf(inquiryDTO.inquiryStatus!!)
        }
        InquiryDTO(inquiry)
    }

    override fun delete(inquiryId: Long) = inquiryRepository.delete(
        inquiryRepository.findByIdOrNull(inquiryId) ?: throw NoSuchElementException("Inquiry $inquiryId not found")
    )
}