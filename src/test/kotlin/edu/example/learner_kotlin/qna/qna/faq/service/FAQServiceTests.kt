package edu.example.learner_kotlin.qna.faq.service

import edu.example.learner_kotlin.qna.faq.dto.FAQDTO
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class FAQServiceTests {
    @Autowired
    private lateinit var faqService: FAQService

    @Test
    fun testRead() {
        val faqId = 1L

        faqService.read(faqId).run {
            assertEquals(faqId, this.faqId)
        }
    }

    @Test
    fun testReadAll() {
        faqService.readAll().run {
            assertEquals(20, this.size)
        }
    }

    @Test
    fun testReadByCategory() {
        val faqCategory = FAQCategory.COURSE

        faqService.readByCategory(faqCategory).run {
            assertEquals(10, this.size)
        }
    }

    @Test
    @Transactional
    fun testRegister() {
        val faqDTO = FAQDTO().apply {
            faqTitle = "service test"
            faqContent = "service test"
            faqCategory = FAQCategory.ORDER.name
        }

        faqService.register(faqDTO).run {
            assertEquals("ORDER", this.faqCategory)
        }
    }

    @Test
    @Transactional
    fun testUpdate() {
        val faqDTO = FAQDTO().apply {
            faqId = 1L
            faqTitle = "update test"
            faqContent = "update test"
            faqCategory = FAQCategory.ORDER.name
        }

        faqService.update(faqDTO).run {
            assertEquals("update test", this.faqTitle)
        }
    }

    @Test
    @Transactional
    fun testDelete() {
        val faqId = 1L

        faqService.delete(faqId).run {
            assertThrows<NoSuchElementException> { faqService.read(faqId) }
        }
    }
}