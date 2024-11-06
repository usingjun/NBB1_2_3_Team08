package edu.example.learner_kotlin.member_video.service

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member_video.dto.MemberVideoDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class MemberVideoServiceTests {
    @Autowired
    lateinit var memberVideoService: MemberVideoService

    @Test
    fun testRead() {
        val memberVideoId = 1L

        memberVideoService.read(memberVideoId).run {
            assertEquals(20L, this.studyTime)
        }
    }

    @Test
    @Transactional
    fun testCreate() {
        val memberVideoDTO = MemberVideoDTO().apply {
            this.watched = true
            this.studyTime = 35L
            this.memberId = 2L
            this.videoId = 2L
        }

        memberVideoService.create(memberVideoDTO).run {
            assertEquals(4L, this.id)
        }
    }

    @Test
    @Transactional
    fun testUpdate() {
        val memberVideoDTO = MemberVideoDTO().apply {
            this.studyTime = 100L
            this.memberId = 1L
            this.videoId = 2L
        }
        memberVideoService.update(memberVideoDTO).run {
            log.info(this.toString())
            assertEquals(2L, this.id)
        }
    }

    @Test
    @Transactional
    fun testDelete() {
        val memberVideoId = 1L

        memberVideoService.delete(memberVideoId).run {
            assertThrows<NoSuchElementException> {
                memberVideoService.delete(memberVideoId)
            }
        }
    }

    @Test
    fun testReadByMemberIdAndVideoId() {
        val memberId = 1L
        val videoId = 1L

        memberVideoService.readByMemberIdAndVideoId(memberId, videoId).run {
            assertEquals(1L, this.id)
        }
    }

    @Test
    fun testReadByMemberIdAndVideoIdFailure() {
        val memberId = 2L
        val videoId = 2L

        assertThrows<NoSuchElementException> {
            memberVideoService.readByMemberIdAndVideoId(memberId, videoId)
        }
    }

    @Test
    fun testGetAverageByVideoId() {
        val videoId = 3L

        memberVideoService.getAverageByVideoId(videoId).run {
            assertEquals(0.0, this)
        }
    }

    @Test
    fun testIsPresentByMemberIdAndVideoId() {
        val memberId = 1L
        val videoId = 1L

        memberVideoService.isPresentByMemberIdAndVideoId(memberId, videoId).run {
            assertTrue(this)
        }
    }
}