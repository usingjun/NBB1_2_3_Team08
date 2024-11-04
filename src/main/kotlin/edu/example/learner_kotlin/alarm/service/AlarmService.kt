package edu.example.learner_kotlin.alarm.service

import edu.example.learner_kotlin.alarm.entity.Alarm
import edu.example.learner_kotlin.alarm.repository.AlarmRepository
import edu.example.learner_kotlin.alarm.dto.AlarmDTO
import edu.example.learner_kotlin.alarm.entity.AlarmType
import edu.example.learner_kotlin.alarm.entity.Priority
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.service.MemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val sseService: SseService,
    private val memberService: MemberService
) {

    @Transactional
    fun createAlarm(memberId: Long, alarmContent: String, alarmTitle: String): AlarmDTO {

        val alarm = Alarm(
            member = Member().apply { this.memberId = memberId },
            alarmContent = alarmContent,
            alarmTitle = alarmTitle
        )

        alarmRepository.save(alarm)
        sseService.sendToMember(memberId, alarmContent)

        return AlarmMapper.toDTO(alarm)
    }

    fun getAlarmsByMember(memberId: Long): List<AlarmDTO> {
        val alarms = alarmRepository.findByMember(Member(memberId))
        return alarms.map { AlarmMapper.toDTO(it) }
    }

    fun getAlarmById(alarmId: Long): AlarmDTO {
        val alarm = alarmRepository.findById(alarmId).orElseThrow { RuntimeException("Alarm not found") }
        return AlarmMapper.toDTO(alarm)
    }

    @Transactional
    fun updateAlarm(alarmId: Long, alarmDTO: AlarmDTO): AlarmDTO {
        val alarm = alarmRepository.findById(alarmId).orElseThrow { RuntimeException("Alarm not found") }

        alarm.alarmContent = alarmDTO.alarmContent
        alarm.alarmType = AlarmType.valueOf(alarmDTO.alarmType ?: "INFO")
        alarm.priority = Priority.valueOf(alarmDTO.priority ?: "MEDIUM")

        return AlarmMapper.toDTO(alarmRepository.save(alarm))
    }

    fun deleteAlarm(alarmId: Long) {
        val alarm = alarmRepository.findById(alarmId).orElseThrow { RuntimeException("Alarm not found") }
        alarmRepository.delete(alarm)
    }
}
