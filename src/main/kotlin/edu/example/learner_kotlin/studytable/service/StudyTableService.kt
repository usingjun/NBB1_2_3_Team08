package edu.example.learner_kotlin.studytable.service

import edu.example.learner_kotlin.studytable.dto.StudyTableDTO
import java.time.LocalDate

interface StudyTableService {
    fun read(studyTableId: Long): StudyTableDTO
    fun readByDate(memberId: Long): Boolean
    fun readDTOByDate(memberId: Long): StudyTableDTO
    fun register(studyTableDTO: StudyTableDTO): StudyTableDTO
    fun update(studyTableDTO: StudyTableDTO): StudyTableDTO
    fun delete(studyTableDTO: StudyTableDTO)
    fun getWeeklyStudyTime(memberId: Long, localDate: LocalDate): Int
    fun getYearlyStudyTime(memberId: Long, year: Int): Int
    fun getWeeklyCompleted(memberId: Long, localDate: LocalDate): Int
    fun getMonthlyCompleted(memberId: Long, month: Int): Int
    fun getYearlyCompleted(memberId: Long, year: Int): Int
    fun getWeeklySummary(memberId: Long, localDate: LocalDate): List<Array<Any>>
    fun getYearlySummary(memberId: Long, year: Int): List<Array<Any>>
}