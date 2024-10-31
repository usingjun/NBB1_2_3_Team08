package edu.example.learner_kotlin.studytable.repository

import edu.example.learner_kotlin.studytable.entity.StudyTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface StudyTableRepository : JpaRepository<StudyTable, Long> {
    @Query("select coalesce(sum(st.studyTime), 0) from StudyTable st where st.member.memberId = :memberId and st.studyDate between :startDate and :endDate")
    fun getWeeklyStudyTime(
        @Param("memberId") memberId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Int

    @Query("select coalesce(sum(st.studyTime), 0) from StudyTable st where st.member.memberId =:memberId and year(st.studyDate) = :year")
    fun getYearlyStudyTime(@Param("memberId") memberId: Long, @Param("year") year: Int): Int

    @Query("select coalesce(sum(st.completed), 0) from StudyTable st where st.member.memberId = :memberId and st.studyDate between :startDate and :endDate")
    fun getWeeklyCompleted(
        @Param("memberId") memberId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Int

    @Query("select coalesce(sum(st.completed), 0) from StudyTable st where st.member.memberId = :memberId and month(st.studyDate) = :month")
    fun getMonthlyCompleted(@Param("memberId") memberId: Long, @Param("month") month: Int): Int

    @Query("select coalesce(sum(st.completed), 0) from StudyTable st where st.member.memberId =:memberId and year(st.studyDate) = :year")
    fun getYearlyCompleted(@Param("memberId") memberId: Long, @Param("year") year: Int): Int

    @Query("select st.studyDate, st.completed from StudyTable st where st.member.memberId = :memberId and st.studyDate between :startDate and :endDate order by st.studyDate")
    fun getWeeklySummary(
        @Param("memberId") memberId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): List<Array<Any>>

    @Query("select month(st.studyDate), floor((day(st.studyDate) + 6) / 7) as weekNumber, sum(st.completed) from StudyTable st where st.member.memberId = :memberId and year(st.studyDate) = :year group by month(st.studyDate), floor((day(st.studyDate) + 6) / 7) order by month(st.studyDate), floor((day(st.studyDate) + 6) / 7)")
    fun getYearlySummary(@Param("memberId") memberId: Long, @Param("year") year: Int): List<Array<Any>>

    @Query("select st from StudyTable st where st.studyDate = :date and st.member.memberId = :memberId")
    fun findByDate(@Param("date") date: LocalDate, @Param("memberId") memberId: Long): StudyTable?
}