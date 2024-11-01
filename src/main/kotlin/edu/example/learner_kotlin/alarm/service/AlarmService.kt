//package edu.example.learner_kotlin.alarm.service
//
//
//
//import edu.example.learner_kotlin.alarm.alarm.AlarmDTO
//import edu.example.learner_kotlin.alarm.entity.Alarm
//import edu.example.learner_kotlin.alarm.entity.AlarmType
//import edu.example.learner_kotlin.alarm.exception.AlarmException
//import edu.example.learner_kotlin.alarm.repository.AlarmRepository
//import edu.example.learner_kotlin.member.entity.Member
//import edu.example.learner_kotlin.member.repository.MemberRepository
//import jakarta.transaction.Transactional
//
//import org.springframework.stereotype.Service
//
//@Service
//@Transactional
//
//class AlarmService (
//    private val alarmRepository: AlarmRepository,
//    private  val memberRepository: MemberRepository,)
//{
//
//    fun findByAlarmTitle(alarmTitle: String?): AlarmDTO {
//        val alarm = alarmRepository.findByAlarmTitle(alarmTitle!!)
//
//        return AlarmDTO(alarm)
//    }
//
//    fun read(alarmId: Long): AlarmDTO {
//        val alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmException.ALARM_NOT_FOUND::get)
//        return AlarmDTO(alarm)
//    }
//
//    fun findAllAlarms(): List<AlarmDTO> {
//        val alarms = alarmRepository!!.findAll()
//        val alarmDTOs: MutableList<AlarmDTO> = ArrayList<AlarmDTO>()
//        for (alarm in alarms) {
//            alarmDTOs.add(AlarmDTO(alarm))
//        }
//        return alarmDTOs
//    }
//
//    fun add(alarmDTO: AlarmDTO): AlarmDTO {
//        val save: Alarm = alarmRepository!!.save(alarmDTO.toEntity(alarmDTO))
//        val member: Member = memberRepository!!.findById(alarmDTO.getMemberId())
//            .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException)
//        //        member.getAlarmList().add(save);
//        return AlarmDTO(save)
//    }
//
//    fun update(alarmDTO: AlarmDTO): AlarmDTO {
//        //alarmDTO에 있는 번호로 알람 조회
//        val alarm = alarmRepository!!.findById(alarmDTO.getAlarmId()).orElseThrow(AlarmException.ALARM_NOT_FOUND::get)
//        //alrarm에 있는 정보들 변경
//        alarm.changeAlarmTitle(alarmDTO.getAlarmTitle())
//        alarm.changeAlarmStatus(alarmDTO.isAlarmStatus())
//        alarm.changeAlarmType(AlarmType.valueOf(alarmDTO.getAlarmType()))
//        alarm.changePriority(Priority.valueOf(alarmDTO.getPriority()))
//        alarm.changeContent(alarmDTO.getAlarmContent())
//        // 알람 저장
//        alarmRepository.save(alarm)
//
//        return AlarmDTO(alarm)
//    }
//
//    fun delete(alarmId: Long) {
//        alarmRepository!!.deleteById(alarmId)
//    }
//
//    fun listAlarmsForMember(memberId: Long): List<AlarmDTO> {
//        val member: Member =
//            memberRepository.findById(memberId).orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException)
//
//        val byMember: List<Alarm> = alarmRepository.findByMember(member)
//
//        val alarmDTOs: MutableList<AlarmDTO> = ArrayList<AlarmDTO>()
//        for (alarm in byMember) {
//            alarmDTOs.add(AlarmDTO(alarm))
//        }
//
//        return alarmDTOs
//    }
//}