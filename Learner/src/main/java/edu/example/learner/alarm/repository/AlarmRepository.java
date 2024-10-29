package edu.example.learner.alarm.repository;

import edu.example.learner.alarm.entity.Alarm;
import edu.example.learner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Alarm findByAlarmTitle(String alarmTitle);
    List<Alarm> findByMember(Member member);
}
