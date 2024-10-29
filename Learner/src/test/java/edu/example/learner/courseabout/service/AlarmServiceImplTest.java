package edu.example.learner.courseabout.service;

import edu.example.learner.alarm.dto.AlarmDTO;
import edu.example.learner.alarm.entity.Alarm;
import edu.example.learner.alarm.entity.AlarmType;
import edu.example.learner.alarm.service.AlarmService;
import edu.example.learner.alarm.entity.Priority;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import edu.example.learner.member.repository.MemberRepository;
import edu.example.learner.alarm.repository.AlarmRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class AlarmServiceImplTest {

    @Autowired
    private AlarmService alarmService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AlarmRepository alarmRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .nickname("test")
                .role(Role.USER)
                .email("test@example.com")
                .password("test")
                .phoneNumber("010-1111-1111")
                .introduction("테스트")
                .build();
        memberRepository.save(member);

        // 여러 개의 알람 추가
        for (int i = 1; i <= 3; i++) {
            Alarm alarm = Alarm.builder()
                    .alarmTitle("Alarm " + i)
                    .alarmContent("This is test alarm " + i)
                    .priority(Priority.LOW)
                    .member(member)
                    .alarmStatus(false)
                    .alarmType(AlarmType.INQUIRY)
                    .createdAt(LocalDateTime.now())
                    .build();
            alarmRepository.save(alarm);
//            member.getAlarmList().add(alarm);

        }
    }

    @Test
    void addAlarm() {
        // given
        AlarmDTO newAlarmDTO = AlarmDTO.builder()
                .memberId(1L)
                .alarmTitle("New Test Alarm")
                .alarmContent("New alarm content.")
                .priority(Priority.HIGH.name())
                .memberId(member.getMemberId())
                .alarmStatus(false)
                .createdAt(LocalDateTime.now())
                .build();
        // when
        AlarmDTO savedAlarmDTO = alarmService.add(newAlarmDTO); // assuming there is an addAlarm method
        Alarm alarm = savedAlarmDTO.toEntity(savedAlarmDTO);
        // then
        assertNotNull(alarm.getAlarmId());
        assertEquals("New Test Alarm", alarm.getAlarmTitle());
    }

    @Test
    void readAlarm() {
        // given
        List<Alarm> alarms = alarmRepository.findByMember(member);
        Alarm alarm = alarms.get(0);
        AlarmDTO alarmDTO = new AlarmDTO(alarm);

        // when
        AlarmDTO foundAlarmDTO = alarmService.read(alarmDTO.getAlarmId()); // assuming there is a readAlarm method

        // then
        assertEquals(alarmDTO.getAlarmId(), foundAlarmDTO.getAlarmId());
        assertEquals("Alarm 1", foundAlarmDTO.getAlarmTitle());
    }

    @Test
    void updateAlarm() {
        // given
        List<Alarm> alarms = alarmRepository.findByMember(member);
        Alarm alarm = alarms.get(0);
        AlarmDTO alarmDTO = new AlarmDTO(alarm);
        alarmDTO.setAlarmTitle("Updated Test Alarm");
        alarmDTO.setPriority(Priority.HIGH.name());
        alarmDTO.setAlarmType(AlarmType.INQUIRY.name());

        // when
        alarmService.update(alarmDTO);

        // then
        Alarm updatedAlarm = alarmRepository.findById(alarmDTO.getAlarmId()).orElseThrow();
        assertEquals("Updated Test Alarm", updatedAlarm.getAlarmTitle());
    }

    @Test
    void deleteAlarm() {
        // given
        List<Alarm> alarms = alarmRepository.findByMember(member);
        Long alarmId = alarms.get(0).getAlarmId();

        // when
        alarmService.delete(alarmId);

        // then
        assertFalse(alarmRepository.findById(alarmId).isPresent());
    }

    @Test
    void listAlarmsForMember() {
        // given
        Long memberId = member.getMemberId();

        // when
        List<AlarmDTO> alarmsForMember = alarmService.listAlarmsForMember(memberId); // a

        // then
        assertFalse(alarmsForMember.isEmpty());
        assertEquals(3, alarmsForMember.size()); // 3개의 알람이 추가되었으므로
    }
}
