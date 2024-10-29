package edu.example.learner.courseabout.course.entity;


import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.coursereview.entity.Review;
import edu.example.learner.courseabout.news.entity.NewsEntity;
import edu.example.learner.courseabout.order.entity.Order;
import edu.example.learner.courseabout.order.entity.OrderItem;
import edu.example.learner.courseabout.video.entity.Video;
import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"videos","newsEntities"})
@Table(name = "course")
@EntityListeners(AuditingEntityListener.class)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @NotNull
    private String courseName;

    @NotEmpty
    private String courseDescription;

    @Enumerated(EnumType.STRING)
    private CourseAttribute courseAttribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname",referencedColumnName = "nickname")
    private Member member;

    @NotNull
    private Long coursePrice;
    @NotNull
    @Max(5)
    @Min(1)
    private Integer courseLevel;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MemberCourse> memberCourses = new ArrayList<>();

    @OneToMany(mappedBy = "courseNews", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<NewsEntity> newsEntities = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CourseInquiry> courseInquiries = new ArrayList<>();

    @OneToMany(mappedBy = "course",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems= new ArrayList<>();



    private boolean sale;

    public void changeCourseStatus(CourseAttribute courseStatus) {
        this.courseAttribute = courseStatus;
    }

    public void changeCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void changeCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void changePrice(Long coursePrice) {
        this.coursePrice = coursePrice;
    }

    public void changeCourseLevel(Integer courseLevel) {
        this.courseLevel = courseLevel;
    }

    public void changeSale(boolean sale) {
        this.sale = sale;
    }

    public void changeCourseCreatedDate(LocalDateTime courseCreatedDate) {
        this.courseCreatedDate = courseCreatedDate;
    }

    public void changeCourseModifiedDate(LocalDateTime courseModifiedDate) {
        this.courseModifiedDate = courseModifiedDate;
    }

    @CreatedDate
    private LocalDateTime courseCreatedDate;

    @LastModifiedDate
    private LocalDateTime courseModifiedDate;
}
