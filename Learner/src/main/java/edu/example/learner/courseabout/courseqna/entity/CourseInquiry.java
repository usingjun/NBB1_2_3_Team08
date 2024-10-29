package edu.example.learner.courseabout.courseqna.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="course_inquiry")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CourseInquiry{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryId;         //문의 Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;          //문의 강의 Id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          //문의자 Id

    private String inquiryTitle;    //문의 제목
    private String inquiryContent;  //문의 내용

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @Enumerated(EnumType.STRING)
    private InquiryStatus inquiryStatus;

    private Long inquiryGood;       //문의 좋아요 수


    // 문의 삭제 시 관련된 답변도 삭제되도록 설정
    @OneToMany(mappedBy = "courseInquiry", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<CourseAnswer> answers;

    public void changeInqueryTitle(String inquiryTitle){
        this.inquiryTitle = inquiryTitle;
    }

    public void changeInquiryContent(String inquiryContent){
        this.inquiryContent = inquiryContent;
    }

    public void changeInquiryStatus(InquiryStatus inquiryStatus){
        this.inquiryStatus = inquiryStatus;
    }

    public void changeInquiryGood(Long inquiryGood){
        this.inquiryGood = inquiryGood;
    }
}
