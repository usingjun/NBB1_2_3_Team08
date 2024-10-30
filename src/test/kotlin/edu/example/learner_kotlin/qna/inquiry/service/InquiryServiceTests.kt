package edu.example.learner_kotlin.qna.inquiry.service

import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class InquiryServiceTests {
    @Autowired
    lateinit var inquiryService: InquiryService
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun testRead() {
        val inquiryId = 1L

        inquiryService.read(inquiryId).run {
            assertEquals(inquiryId, this.inquiryId)
        }
    }

    @Test
    fun testReadByMemberId() {
        val memberId = 1L

        inquiryService.readByMemberId(memberId).run {
            assertEquals(10, this.size)
        }
    }

    @Test
    fun testReadAll() {
        inquiryService.readAll().run {
            assertEquals(10, this.size)
        }
    }

    @Test
    @Transactional
    fun testRegister() {
        val inquiryDTO = InquiryDTO().apply {
            inquiryTitle = "register test"
            inquiryContent = "register test"
            memberId = 1L
            memberNickname = "test"
        }

        inquiryService.register(inquiryDTO).run {
            assertEquals("register test", this.inquiryTitle)
        }
    }

    @Test
    @Transactional
    fun testUpdate() {
        val inquiryDTO = InquiryDTO().apply {
            inquiryId = 1L
            inquiryTitle = "update test"
            inquiryContent = "update test"
            memberId = 1L
        }

        inquiryService.update(inquiryDTO).run {
            assertEquals("update test", this.inquiryTitle)
        }
    }

    @Test
    @Transactional
    fun testDelete() {
        val inquiryId = 1L

        inquiryService.delete(inquiryId).run {
            assertThrows<NoSuchElementException> {
                inquiryService.read(inquiryId)
            }
        }
    }
}