package edu.example.learner.alarm.service;

import edu.example.learner.alarm.dto.AlarmDTO;
import edu.example.learner.alarm.entity.Alarm;
import edu.example.learner.alarm.entity.AlarmType;
import edu.example.learner.alarm.exception.AlarmException;
import edu.example.learner.alarm.repository.AlarmRepository;
import edu.example.learner.alarm.entity.Priority;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.exception.MemberException;
import edu.example.learner.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;

    private final MemberRepository memberRepository;

    @Override
    public AlarmDTO findByAlarmTitle(String alarmTitle) {

        Alarm alarm = alarmRepository.findByAlarmTitle(alarmTitle);

        return new AlarmDTO(alarm);
    }

    @Override
    public AlarmDTO read(Long alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmException.ALARM_NOT_FOUND::get);
        return new AlarmDTO(alarm);
    }

    @Override
    public List<AlarmDTO> findAllAlarms() {
        List<Alarm> alarms = alarmRepository.findAll();
        List<AlarmDTO> alarmDTOs = new ArrayList<>();
        for (Alarm alarm : alarms) {
            alarmDTOs.add(new AlarmDTO(alarm));
        }
        return alarmDTOs;
    }

    @Override
    public AlarmDTO add(AlarmDTO alarmDTO) {
        Alarm save = alarmRepository.save(alarmDTO.toEntity(alarmDTO));
        Member member = memberRepository.findById(alarmDTO.getMemberId()).orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
//        member.getAlarmList().add(save);
        return new AlarmDTO(save);
    }

    @Override
    public AlarmDTO update(AlarmDTO alarmDTO) {
        //alarmDTO에 있는 번호로 알람 조회
        Alarm alarm = alarmRepository.findById(alarmDTO.getAlarmId()).orElseThrow(AlarmException.ALARM_NOT_FOUND::get);
        //alrarm에 있는 정보들 변경
        alarm.changeAlarmTitle(alarmDTO.getAlarmTitle());
        alarm.changeAlarmStatus(alarmDTO.isAlarmStatus());
        alarm.changeAlarmType(AlarmType.valueOf(alarmDTO.getAlarmType()));
        alarm.changePriority(Priority.valueOf(alarmDTO.getPriority()));
        alarm.changeContent(alarmDTO.getAlarmContent());
        // 알람 저장
        alarmRepository.save(alarm);

        return new AlarmDTO(alarm);
    }

    @Override
    public void delete(Long alarmId) {
        alarmRepository.deleteById(alarmId);
    }

    @Override
    public List<AlarmDTO> listAlarmsForMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);

        List<Alarm> byMember = alarmRepository.findByMember(member);

        List<AlarmDTO> alarmDTOs = new ArrayList<>();
        for (Alarm alarm : byMember) {
            alarmDTOs.add(new AlarmDTO(alarm));
        }

        return alarmDTOs;
    }
}
