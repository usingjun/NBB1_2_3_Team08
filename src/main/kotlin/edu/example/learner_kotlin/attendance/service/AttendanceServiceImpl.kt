package edu.example.learner_kotlin.attendance.service

import edu.example.learner_kotlin.attendance.dto.AttendanceDTO
import edu.example.learner_kotlin.attendance.entity.Attendance
import edu.example.learner_kotlin.attendance.repository.AttendanceRepository
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

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

    override fun isAttended(memberId: Long, date: LocalDate): Boolean =
        attendanceRepository.findByMemberIdAndDate(memberId, date).isNotNull()

    @Transactional
    override fun create(attendanceDTO: AttendanceDTO): AttendanceDTO? = run {
        attendanceRepository.findByMemberIdAndDate(attendanceDTO.memberId!!, LocalDate.now())?.let {
            throw Exception("Attendance ${it.attendanceId} already exists.")
        }
        AttendanceDTO(attendanceRepository.save(Attendance().apply {
            continuous = attendanceDTO.continuous
            member = Member().apply { memberId = attendanceDTO.memberId }
        }))
    }

    @Transactional
    override fun update(attendanceDTO: AttendanceDTO): AttendanceDTO = run {
        val attendance = attendanceRepository.findByIdOrNull(attendanceDTO.attendanceId)
            ?: throw NoSuchElementException("Attendance $attendanceDTO.attendanceId not found}")
        with(attendance) {
            continuous = attendanceDTO.continuous
        }
        AttendanceDTO(attendance)
    }

    @Transactional
    override fun delete(attendanceId: Long) = attendanceRepository.delete(
        attendanceRepository.findByIdOrNull(attendanceId)
            ?: throw NoSuchElementException("Attendance $attendanceId not found")
    )

    override fun getWeeklySummary(memberId: Long, date: LocalDate): List<AttendanceDTO> = run {
        val startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        attendanceRepository.findByMemberIdAndWeek(memberId, startDate, endDate).map { AttendanceDTO(it) }
    }

    override fun getYearlySummary(memberId: Long, year: Int): List<Array<Any>> =
        attendanceRepository.findByMemberIdAndYear(memberId, year)

    override fun getContinuous(memberId: Long, date: LocalDate): Int? =
        attendanceRepository.findContinuousByMemberIdAndDate(memberId, date)

    override fun getYearlyAttendanceDays(memberId: Long, year: Int): Int =
        attendanceRepository.countAttendanceDaysOfYearByMemberIdAndYear(memberId, year)

    fun Attendance?.isNotNull(): Boolean = this != null
}