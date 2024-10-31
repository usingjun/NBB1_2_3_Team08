package edu.example.learner_kotlin.attendance.service

import edu.example.learner_kotlin.log
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import kotlin.test.assertEquals

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class AttendanceServiceTests {
    @Autowired
    lateinit var attendanceService: AttendanceService

    @Test
    fun testRead() {
        val attendanceId = 1L

        attendanceService.read(attendanceId).run {
            assertEquals(attendanceId, this.attendanceId)
        }
    }

    @Test
    fun testReadByMemberIdAndDate() {
        val memberId = 1L
        val date = LocalDate.now()

        attendanceService.readByMemberIdAndDate(memberId, date).run {
            assertEquals(memberId, this.memberId)
        }
    }

    @Test
    fun testReadByMemberId() {
        val memberId = 1L

        attendanceService.readByMemberId(memberId).run {
            log.info(this.toString())
            assertEquals(1, this.size)
        }
    }
}