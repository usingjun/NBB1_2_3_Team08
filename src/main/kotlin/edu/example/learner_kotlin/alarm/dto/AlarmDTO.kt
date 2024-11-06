package edu.example.learner_kotlin.alarm.dto


data class AlarmDTO(
    val alarmId : Long? = null,
    var alarmContent : String? = "",
    var alarmTitle : String? = "",
    var alarmType: String? = null,
    var createdAt : String? = null,
    var priority: String?= null,
    var alarmStatus: Boolean =false,
    var memberId : Long? = null,
) {

}