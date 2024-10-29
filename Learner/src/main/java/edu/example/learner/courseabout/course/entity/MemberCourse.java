package edu.example.learner.courseabout.course.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_course")
@EntityListeners(AuditingEntityListener.class)
public class MemberCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_course_id")
    private Long memberCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId",referencedColumnName = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId",referencedColumnName = "course_id")
    private Course course;

    @CreatedDate
    @Column(name = "purchase_date")
    private Date purchaseDate;
}
