package edu.example.learner_kotlin.qna.faq.repository

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class FAQRepositoryTests {
    @Autowired
    lateinit var faqRepository : FAQRepository

    @Test
    fun testFindByFaqCategory() {
        val faqCategory = FAQCategory.COURSE

        faqRepository.findByFaqCategory(faqCategory).run {
            assertEquals(10, this.size)
        }
    }
}