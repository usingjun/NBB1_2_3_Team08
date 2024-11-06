package edu.example.learner_kotlin.attendance.repository

import edu.example.learner_kotlin.attendance.entity.Attendance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
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

    @Query(
            value = "select month(a.attendance_date) as monthNumber, week(a.attendance_date, 0) - week(last_day(a.attendance_date - interval 1 month) + interval 1 day, 0) + 1 as weekNumber from Attendance a where a.member_id = :memberId and year(a.attendance_date) = :year group by monthNumber, weekNumber",
        nativeQuery = true
    )
    fun findByMemberIdAndYear(@Param("memberId") memberId: Long, @Param("year") year: Int): List<Array<Any>>

    @Query("select a.continuous from Attendance a where a.member.memberId = :memberId and a.attendanceDate = :date")
    fun findContinuousByMemberIdAndDate(@Param("memberId") memberId: Long, @Param("date") date: LocalDate): Int?

    @Query("select count(a) from Attendance a where a.member.memberId = :memberId and year(a.attendanceDate) = :year")
    fun countAttendanceDaysOfYearByMemberIdAndYear(@Param("memberId") memberId: Long, @Param("year") year: Int): Int
}