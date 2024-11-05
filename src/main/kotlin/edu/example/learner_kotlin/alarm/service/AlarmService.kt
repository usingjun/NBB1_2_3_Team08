package edu.example.learner_kotlin.alarm.service

import edu.example.learner_kotlin.alarm.entity.Alarm
import edu.example.learner_kotlin.alarm.repository.AlarmRepository
import edu.example.learner_kotlin.alarm.dto.AlarmDTO
import edu.example.learner_kotlin.alarm.entity.AlarmType
import edu.example.learner_kotlin.alarm.entity.Priority
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.member.service.MemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AlarmService(
    private val alarmRepository: AlarmRepository,
    private val sseService: SseService,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService
) {

    @Transactional
    fun createAlarm(memberId: Long, alarmContent: String, alarmTitle: String): AlarmDTO {
        val member = memberRepository.findById(memberId).orElseThrow {
            IllegalArgumentException("Member not found with ID: $memberId")
        }

        val alarm = Alarm(
            member = member,
            alarmContent = alarmContent,
            alarmTitle = alarmTitle
        )

        alarmRepository.save(alarm)
        sseService.sendToMember(memberId, alarmTitle, alarmContent)
        log.info("Created alarm: ${AlarmMapper.toDTO(alarm)}")
        return AlarmMapper.toDTO(alarm)
    }

    @Transactional
    fun createAlarmSendToUser(alarmContent: String, alarmTitle: String) {
        val users = memberRepository.findAll().filter { it.role == Role.USER }

        users.forEach { user ->
            val alarm = Alarm(
                member = user,
                alarmContent = alarmContent,
                alarmTitle = alarmTitle
            )
            alarmRepository.save(alarm)
            sseService.sendToMember(user.memberId!!, alarmTitle, alarmContent)
        }

        log.info("Alarm sent to all users with Role.USER: $alarmTitle - $alarmContent")
    }

    fun getAlarmsByMember(memberId: Long): List<AlarmDTO> {
        val member = memberRepository.findById(memberId).orElseThrow {
            IllegalArgumentException("Member not found with ID: $memberId")
        }
        val alarms = alarmRepository.findByMember(member)
        return alarms.map { AlarmMapper.toDTO(it) }
    }

    fun getAlarmById(alarmId: Long): AlarmDTO {
        val alarm = alarmRepository.findById(alarmId).orElseThrow {
            IllegalArgumentException("Alarm not found with ID: $alarmId")
        }
        return AlarmMapper.toDTO(alarm)
    }

    @Transactional
    fun updateAlarm(alarmId: Long, alarmDTO: AlarmDTO): AlarmDTO {
        val alarm = alarmRepository.findById(alarmId).orElseThrow {
            IllegalArgumentException("Alarm not found with ID: $alarmId")
        }

        alarm.apply {
            alarmContent = alarmDTO.alarmContent
            alarmType = AlarmType.valueOf(alarmDTO.alarmType ?: "INFO")
            priority = Priority.valueOf(alarmDTO.priority ?: "MEDIUM")
        }

        return AlarmMapper.toDTO(alarmRepository.save(alarm))
    }

    fun deleteAlarm(alarmId: Long) {
        val alarm = alarmRepository.findById(alarmId).orElseThrow {
            IllegalArgumentException("Alarm not found with ID: $alarmId")
        }
        alarmRepository.delete(alarm)
    }
}
