package edu.example.learner_kotlin.qna.answer.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class AnswerRepositoryTests {
    @Autowired
    private lateinit var answerRepository: AnswerRepository

    @Test
    fun testFindByInquiryId() {
        val inquiryId = 1L

        answerRepository.findByInquiryId(inquiryId).run {
            assertEquals(inquiryId, this?.answerId)
        }
    }

    @Test
    @Transactional
    fun testDeleteByInquiryId() {
        val inquiryId = 1L

        answerRepository.deleteByInquiryId(inquiryId).run {
            assertThrows<NoSuchElementException> {
                answerRepository.findByInquiryId(inquiryId) ?: throw NoSuchElementException()
            }
        }
    }
}