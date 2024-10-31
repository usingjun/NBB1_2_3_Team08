package edu.example.learner_kotlin.attendance.repository

import edu.example.learner_kotlin.attendance.entity.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface AttendanceRepository : JpaRepository<Attendance, Long> {
    @Query("select a from Attendance a where a.member.memberId = :memberId and a.attendanceDate = :date")
    fun findByMemberIdAndDate(@Param("memberId") memberId: Long, @Param("date") date: LocalDate): Attendance?

    @Query("select a from Attendance a where a.member.memberId = :memberId")
    fun findByMemberId(@Param("memberId") memberId: Long): List<Attendance>

    @Query("select a from Attendance a where a.member.memberId = :memberId and a.attendanceDate between :startDate and :endDate order by a.attendanceDate")
    fun findByMemberIdAndWeek(
        @Param("memberId") memberId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<Attendance>

    @Query("select month(a.attendanceDate), floor((day(a.attendanceDate) + 6) / 7) as weekNumber from Attendance a where a.member.memberId = :memberId and year(a.attendanceDate) = :year group by month(a.attendanceDate), floor((day(a.attendanceDate) + 6) / 7) order by month(a.attendanceDate), floor((day(a.attendanceDate) + 6) / 7)")
    fun findByMemberIdAndYear(@Param("memberId") memberId: Long, @Param("year") year: Int): List<Array<Any>>

    @Query("select a.continuous from Attendance a where a.member.memberId = :memberId and a.attendanceDate = :date")
    fun findContinuousByMemberIdAndDate(@Param("memberId") memberId: Long, @Param("date") date: LocalDate): Int
}