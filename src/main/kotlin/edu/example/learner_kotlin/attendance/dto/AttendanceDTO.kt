package edu.example.learner_kotlin.attendance.dto

import edu.example.learner_kotlin.attendance.entity.Attendance
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class AttendanceDTO(
    var attendanceId: Long? = null,

    var attendanceDate: LocalDate? = null,

    var continuous: Int? = 1,

    @NotBlank
    var memberId: Long? = null,
) {
    constructor(attendance: Attendance) : this(
        attendanceId = attendance.attendanceId,
        attendanceDate = attendance.attendanceDate,
        continuous = attendance.continuous,
        memberId = attendance.member?.memberId
    )
}