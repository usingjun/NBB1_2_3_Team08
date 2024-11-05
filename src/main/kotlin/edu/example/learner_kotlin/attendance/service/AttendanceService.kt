package edu.example.learner_kotlin.attendance.service

import edu.example.learner_kotlin.attendance.dto.AttendanceDTO
import java.time.LocalDate

interface AttendanceService {
    fun read(attendanceId: Long): AttendanceDTO
    fun readByMemberIdAndDate(memberId: Long, date: LocalDate): AttendanceDTO
    fun readByMemberId(memberId: Long): List<AttendanceDTO>
    fun create(attendanceDTO: AttendanceDTO): AttendanceDTO?
    fun update(attendanceDTO: AttendanceDTO): AttendanceDTO
    fun delete(attendanceId: Long)
    fun getWeeklySummary(memberId: Long, date: LocalDate): List<AttendanceDTO>
    fun getYearlySummary(memberId: Long, year: Int): List<Array<Any>>
    fun getContinuous(memberId: Long, date: LocalDate): Int?
    fun isAttended(memberId: Long, date: LocalDate): Boolean
    fun getYearlyAttendanceDays(memberId: Long, year: Int): Int
}