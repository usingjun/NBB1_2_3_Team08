package edu.example.learner_kotlin.attendance.controller

import edu.example.learner_kotlin.attendance.dto.AttendanceDTO
import edu.example.learner_kotlin.attendance.service.AttendanceService
import edu.example.learner_kotlin.log
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/attendances")
class AttendanceController(private val attendanceService: AttendanceService) {
    @GetMapping("/{memberId}/weekly-summary")
    fun getWeeklyAttendance(
        @PathVariable("memberId") memberId: Long,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ) = run {
        val response = mutableMapOf<String, Any>()
        response["weeklySummary"] = attendanceService.getWeeklySummary(memberId, date)
        ResponseEntity.ok(response)
    }

    @GetMapping("/{memberId}/yearly-summary")
    fun getYearlyAttendance(@PathVariable("memberId") memberId: Long, @RequestParam("year") year: Int) = run {
        val response = mutableMapOf<String, Any>()
        response["yearlySummary"] = attendanceService.getYearlySummary(memberId, year)
        response["attendanceDays"] = attendanceService.getYearlyAttendanceDays(memberId, year)
        ResponseEntity.ok(response)
    }

    @GetMapping("/{memberId}/today")
    fun checkTodayAttendance(@PathVariable("memberId") memberId: Long) = run {
        val response = mutableMapOf<String, Any>()
        response["attendance"] = attendanceService.isAttended(memberId, LocalDate.now())
        ResponseEntity.ok(response)
    }

    @GetMapping("/{memberId}/continuous")
    fun getTodayContinuous(@PathVariable("memberId") memberId: Long) = run {
        val response = mutableMapOf<String, Any>()
        response["continuous"] = attendanceService.getContinuous(memberId, LocalDate.now())
        ResponseEntity.ok(response)
    }

    @PostMapping
    fun attend(@Validated @RequestBody attendanceDTO: AttendanceDTO) = run {
        val yesterday = LocalDate.now().minusDays(1)
        if (attendanceService.isAttended(
                attendanceDTO.memberId!!,
                yesterday
            )
        ) {
            val result = attendanceService.create(attendanceDTO.apply {
                this.continuous = attendanceService.getContinuous(attendanceDTO.memberId!!, yesterday) + 1
            })
            ResponseEntity.ok(result)
        } else {
            log.error("You did not attend yesterday!")
            val result = attendanceService.create(attendanceDTO)
            ResponseEntity.ok(result)
        }
    }
}