package edu.example.learner_kotlin.alarm.alarm

import edu.example.learner_kotlin.alarm.entity.Alarm
import edu.example.learner_kotlin.alarm.entity.AlarmType
import edu.example.learner_kotlin.alarm.entity.Priority
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import java.time.LocalDateTime


data class AlarmDTO(
    val alarmId : Long? = null,
    var alarmContent : String? = "",
    var alarmType: String? = null,
    var createdAt : String? = null,
    var priority: String?= null,
    var alarmStatus: Boolean =false,
) {

}