package edu.example.learner_kotlin.alarm.service

import edu.example.learner_kotlin.alarm.entity.Alarm
import edu.example.learner_kotlin.alarm.dto.AlarmDTO
import edu.example.learner_kotlin.alarm.entity.AlarmType
import edu.example.learner_kotlin.alarm.entity.Priority
import edu.example.learner_kotlin.member.entity.Member

object AlarmMapper {
    fun toDTO(alarm: Alarm): AlarmDTO {
        return AlarmDTO(
            alarmId = alarm.alarmId,
            alarmContent = alarm.alarmContent,
            alarmType = alarm.alarmType?.name,
            createdAt = alarm.createdAt.toString(),
            priority = alarm.priority?.name,
            alarmStatus = alarm.alarmStatus,
            memberId = alarm.member!!.memberId
        )
    }

    fun toEntity(alarmDTO: AlarmDTO, member: Member): Alarm {
        return Alarm(
            alarmId = alarmDTO.alarmId,
            alarmContent = alarmDTO.alarmContent,
            alarmType = AlarmType.valueOf(alarmDTO.alarmType ?: "INFO"),
            priority = Priority.valueOf(alarmDTO.priority ?: "MEDIUM"),
            member = member
        )
    }
}