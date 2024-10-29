package edu.example.learner.alarm.dto;

import edu.example.learner.alarm.entity.Alarm;
import edu.example.learner.alarm.entity.Priority;
import edu.example.learner.member.entity.Member;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;


@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AlarmDTO {

    private Long alarmId;

    private Long memberId;

    private String alarmContent;

    private String alarmTitle;

    private String alarmType;

    private LocalDateTime createdAt; // 생성 시간

    private String priority;

    private boolean alarmStatus;

    public Alarm toEntity(AlarmDTO alarmDTO) {
        return Alarm.builder()
                .alarmId(alarmId)
                .alarmContent(alarmContent)
                .alarmTitle(alarmTitle)
                .alarmStatus(alarmStatus)
                .priority(Priority.valueOf(priority))
                .createdAt(createdAt)
                .member(Member.builder().memberId(memberId).build())
                .build();
    }
    public AlarmDTO(Alarm alarm) {
        this.alarmId = alarm.getAlarmId();
        this.memberId = alarm.getMember().getMemberId();
        this.alarmContent = alarm.getAlarmContent();
        this.alarmTitle = alarm.getAlarmTitle();
        this.priority = alarm.getPriority().name();
        this.alarmStatus = alarm.isAlarmStatus();
        this.createdAt = alarm.getCreatedAt();
    }

}
