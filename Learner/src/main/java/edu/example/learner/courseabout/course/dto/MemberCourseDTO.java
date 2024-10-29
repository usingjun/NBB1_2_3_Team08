package edu.example.learner.courseabout.course.dto;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.MemberCourse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MemberCourseDTO {
    private Long courseId;
    private String courseName;
    private String instructor;
    private Date purchaseDate;

    public MemberCourseDTO(MemberCourse memberCourse) {
        this.courseId = memberCourse.getCourse().getCourseId();
        this.courseName = memberCourse.getCourse().getCourseName();
        this.instructor = memberCourse.getCourse().getMember().getNickname();
        this.purchaseDate = memberCourse.getPurchaseDate();
    }
}
