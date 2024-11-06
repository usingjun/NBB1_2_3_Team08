package edu.example.learner_kotlin.studytable.service

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.studytable.dto.StudyTableDTO
import edu.example.learner_kotlin.studytable.entity.StudyTable
import edu.example.learner_kotlin.studytable.repository.StudyTableRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Service
class StudyTableServiceImpl(private val studyTableRepository: StudyTableRepository) : StudyTableService {
    override fun read(studyTableId: Long): StudyTableDTO =
        StudyTableDTO(studyTableRepository.findByIdOrNull(studyTableId) ?: throw NoSuchElementException("StudyTable with ID $studyTableId not found"))

    override fun readByDate(memberId: Long): Boolean =
        studyTableRepository.findByDate(LocalDate.now(), memberId) != null

    override fun readDTOByDate(memberId: Long): StudyTableDTO  =
        StudyTableDTO(studyTableRepository.findByDate(LocalDate.now(), memberId) ?: throw NoSuchElementException("StudyTable does not exist."))

    @Transactional
    override fun register(studyTableDTO: StudyTableDTO): StudyTableDTO =
        StudyTableDTO(studyTableRepository.save(StudyTable().apply {
            studyTime = studyTableDTO.studyTime
            completed = studyTableDTO.completed
            member = Member().apply { memberId = studyTableDTO.memberId }
        }))

    @Transactional
    override fun update(studyTableDTO: StudyTableDTO): StudyTableDTO = run {
        val studyTable = studyTableRepository.findByIdOrNull(studyTableDTO.studyTableId)
            ?: throw NoSuchElementException("StudyTable with ID ${studyTableDTO.studyTableId} not found")
        with(studyTable) {
            this.studyTime += studyTableDTO.studyTime
            this.completed += studyTableDTO.completed
        }
        StudyTableDTO(studyTable)
    }

    @Transactional
    override fun delete(studyTableDTO: StudyTableDTO) =
        studyTableRepository.delete(
            studyTableRepository.findByIdOrNull(studyTableDTO.studyTableId) ?: throw NoSuchElementException("StudyTable with ID ${studyTableDTO.studyTableId} not found")
        )

    override fun getWeeklyStudyTime(memberId: Long, localDate: LocalDate): Int = run {
        val startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        studyTableRepository.getWeeklyStudyTime(memberId, startDate, endDate)
    }

    override fun getYearlyStudyTime(memberId: Long, year: Int): Int =
        studyTableRepository.getYearlyStudyTime(memberId, year)

    override fun getWeeklyCompleted(memberId: Long, localDate: LocalDate): Int = run {
        val startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        studyTableRepository.getWeeklyCompleted(memberId, startDate, endDate)
    }

    override fun getMonthlyCompleted(memberId: Long, month: Int): Int =
        studyTableRepository.getMonthlyCompleted(memberId, month)

    override fun getYearlyCompleted(memberId: Long, year: Int): Int =
        studyTableRepository.getYearlyCompleted(memberId, year)

    override fun getWeeklySummary(memberId: Long, localDate: LocalDate): List<Array<Any>> = run {
        val startDate = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
        val endDate = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
        studyTableRepository.getWeeklySummary(memberId, startDate, endDate)
    }

    override fun getYearlySummary(memberId: Long, year: Int): List<Array<Any>> =
        studyTableRepository.getYearlySummary(memberId, year)
}