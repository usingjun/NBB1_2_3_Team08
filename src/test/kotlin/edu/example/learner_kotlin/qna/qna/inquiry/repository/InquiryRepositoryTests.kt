package edu.example.learner_kotlin.qna.inquiry.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class InquiryRepositoryTests {
    @Autowired
    lateinit var inquiryRepository : InquiryRepository

    @Test
    fun testFindByMemberId() {
        val memberId = 1L

        inquiryRepository.findByMemberId(memberId).run {
            assertEquals(11, this.size)
        }
    }
}