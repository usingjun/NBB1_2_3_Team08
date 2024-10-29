package edu.example.learner.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.MemberCourse;
import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.coursereview.entity.Review;
import edu.example.learner.courseabout.news.entity.HeartNews;
import edu.example.learner.qna.inquiry.entity.Inquiry;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", indexes = @Index(columnList = "email"))
@Setter
@Getter
@ToString(exclude = {"heartNewsList", "memberCourses", "courses", "inquiries", "courseInquiries", "courseAnswers", "reviews"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})  // Hibernate 프록시 필드를 무시
public class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false , unique = true )
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false , unique = true )
    private String nickname;

    @Column(nullable = true)
    private String phoneNumber;

    @Lob  // BLOB 타입으로 처리됨
    private byte[] profileImage;

    private String imageType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = true)
    private String introduction;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<HeartNews> heartNewsList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MemberCourse> memberCourses = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseInquiry> courseInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseAnswer> courseAnswers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void changeProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void changeImageType(String imageType) {
        this.imageType = imageType;
    }
}
