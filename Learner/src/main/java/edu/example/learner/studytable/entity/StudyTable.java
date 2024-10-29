package edu.example.learner.studytable.entity;

import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "study_table")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class StudyTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyTableId;

    @CreatedDate
    private LocalDate studyDate;

    private int studyTime;

    private int completed;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public void changeCompleted(int completed) {
        this.completed = completed;
    }
}
