package edu.example.learner.courseabout.course.dto;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import edu.example.learner.member.entity.Member;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String courseDescription;
    private Long coursePrice;
    private String memberNickname;

    @Min(1)
    @Max(5)
    private Integer courseLevel;

    private String courseAttribute;
    private boolean sale;
    private Long memberId;
    private LocalDateTime createdAt;

    public Course toEntity() {
        return Course.builder()
                .member(Member.builder().nickname(memberNickname).build())
                .courseDescription(courseDescription)
                .courseName(courseName)
                .coursePrice(coursePrice)
                .courseLevel(courseLevel)
                .courseAttribute(CourseAttribute.JAVA)
                .courseCreatedDate(createdAt)
                .sale(false)
                .build();
    }

    public CourseDTO(Course course) {
        this.memberNickname = course.getMember().getNickname();
        this.courseId = course.getCourseId();
        this.courseName = course.getCourseName();
        this.courseDescription = course.getCourseDescription();
        this.coursePrice = course.getCoursePrice();
        this.courseLevel = course.getCourseLevel();
        this.courseAttribute = course.getCourseAttribute().name();
        this.sale = course.isSale();
        this.memberId = course.getMember().getMemberId();
        this.createdAt = course.getCourseCreatedDate();
    }
}
