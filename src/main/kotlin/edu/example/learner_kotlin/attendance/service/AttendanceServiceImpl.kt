package edu.example.learner_kotlin.attendance.service

import edu.example.learner_kotlin.attendance.dto.AttendanceDTO
import edu.example.learner_kotlin.attendance.repository.AttendanceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceServiceImpl(private val attendanceRepository: AttendanceRepository) : AttendanceService {
    override fun read(attendanceId: Long): AttendanceDTO = AttendanceDTO(
        attendanceRepository.findByIdOrNull(attendanceId)
            ?: throw NoSuchElementException("Attendance $attendanceId not found")
    )

    override fun readByMemberIdAndDate(memberId: Long, date: LocalDate): AttendanceDTO = AttendanceDTO(
        attendanceRepository.findByMemberIdAndDate(memberId, date)
            ?: throw NoSuchElementException("Attendance not found")
    )

    override fun readByMemberId(memberId: Long): List<AttendanceDTO> =
        attendanceRepository.findByMemberId(memberId).map { AttendanceDTO(it) }

    override fun create(attendanceDTO: AttendanceDTO): AttendanceDTO = AttendanceDTO()

    override fun update(attendanceDTO: AttendanceDTO): AttendanceDTO {
        TODO("Not yet implemented")
    }

    override fun delete(attendanceId: Long) {
        TODO("Not yet implemented")
    }

    override fun getWeeklySummary(memberId: Long, date: LocalDate): List<AttendanceDTO> {
        TODO("Not yet implemented")
    }

    override fun getYearlySummary(memberId: Long, year: Int): List<Array<Any>> {
        TODO("Not yet implemented")
    }

    override fun getContinuous(memberId: Long, date: LocalDate): Int {
        TODO("Not yet implemented")
    }
}