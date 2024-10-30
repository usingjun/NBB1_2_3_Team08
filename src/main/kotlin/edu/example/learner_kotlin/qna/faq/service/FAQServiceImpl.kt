package edu.example.learner_kotlin.qna.faq.service

import edu.example.learner_kotlin.qna.faq.dto.FAQDTO
import edu.example.learner_kotlin.qna.faq.entity.FAQ
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import edu.example.learner_kotlin.qna.faq.repository.FAQRepository
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FAQServiceImpl(private val faqRepository: FAQRepository, private val modelMapper: ModelMapper) : FAQService {
    override fun read(faqId: Long) =
        FAQDTO(faqRepository.findByIdOrNull(faqId) ?: throw NoSuchElementException("FAQ $faqId not found"))

    override fun readAll() = faqRepository.getAllDTO()

    override fun readByCategory(faqCategory: FAQCategory) =
        faqRepository.findByFaqCategory(faqCategory)

    override fun register(faqDTO: FAQDTO) = FAQDTO(faqRepository.save(modelMapper.map(faqDTO, FAQ::class.java)))

    override fun update(faqDTO: FAQDTO) = run {
        val faq = faqRepository.findByIdOrNull(faqDTO.faqId) ?: throw NoSuchElementException("FAQ not found")
        with(faq) {
            faqTitle = faqDTO.faqTitle
            faqContent = faqDTO.faqContent
            faqCategory = FAQCategory.valueOf(faqDTO.faqCategory.toString())
        }
        FAQDTO(faq)
    }

    override fun delete(faqId: Long) = faqRepository.delete(
        faqRepository.findByIdOrNull(faqId) ?: throw NoSuchElementException("FAQ $faqId not found")
    )
}