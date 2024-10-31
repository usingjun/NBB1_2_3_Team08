package edu.example.learner_kotlin.studytable.controller

import edu.example.learner_kotlin.studytable.dto.StudyTableDTO
import edu.example.learner_kotlin.studytable.service.StudyTableService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/study-tables")
class StudyTableController(val studyTableService: StudyTableService) {
    @GetMapping("/{memberId}/weekly-summary")
    fun readWeeklySummaryByDate(
        @PathVariable("memberId") memberId: Long,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ) = run {
        val response = mutableMapOf<String, Any>()
        response["weeklyStudyTime"] = studyTableService.getWeeklyStudyTime(memberId, date)
        response["weeklyCompleted"] = studyTableService.getWeeklyCompleted(memberId, date)
        response["weeklySummary"] = studyTableService.getWeeklySummary(memberId, date)
        ResponseEntity.ok(response)
    }

    @GetMapping("/{memberId}/yearly-summary")
    fun readYearlySummary(@PathVariable("memberId") memberId: Long, @RequestParam("year") year: Int) = run {
        val response = mutableMapOf<String, Any>()
        response["yearlyCompleted"] = studyTableService.getYearlyCompleted(memberId, year);
        response["yearlyStudyTime"] = studyTableService.getYearlyStudyTime(memberId, year);
        response["yearlySummary"] = studyTableService.getYearlySummary(memberId, year);
        ResponseEntity.ok(response)
    }

    @PostMapping
    fun register(@Validated @RequestBody studyTableDTO: StudyTableDTO) =
        if (studyTableService.readByDate(studyTableDTO.studyTableId!!)) ResponseEntity.ok(studyTableDTO)
        else ResponseEntity.ok(studyTableService.register(studyTableDTO))

    @PutMapping("/{studyTableId}")
    fun update(@PathVariable("studyTableId") studyTableId: Long, @Validated @RequestBody studyTableDTO: StudyTableDTO) =
        ResponseEntity.ok(studyTableService.update(studyTableDTO.apply { this.studyTableId = studyTableDTO.studyTableId }))

    @PutMapping("/today")
    fun updateWithDate(@RequestBody studyTableDTO: StudyTableDTO) = run {
        studyTableDTO.apply {
            studyTableId = studyTableService.readDTOByDate(studyTableDTO.memberId!!).studyTableId
            ResponseEntity.ok(studyTableService.update(studyTableDTO))
        }
    }

    @DeleteMapping("/{studyTableId}")
    fun delete(@PathVariable("studyTableId") studyTableId: Long) =
        studyTableService.delete(studyTableService.read(studyTableId))
}