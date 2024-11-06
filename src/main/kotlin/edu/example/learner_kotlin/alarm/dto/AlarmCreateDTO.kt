package edu.example.learner_kotlin.alarm.dto

data class AlarmCreateDTO(
    val memberId : Long? = null,
    val alarmId : Long? = null,
    var alarmContent : String? = "",
    var alarmType: String? = null,
)
