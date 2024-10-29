package edu.example.learner.courseabout.course.service;

import edu.example.learner.courseabout.course.dto.CourseDTO;
import edu.example.learner.courseabout.course.dto.MemberCourseDTO;
import edu.example.learner.courseabout.course.entity.CourseAttribute;

import java.util.List;

public interface CourseService {
    CourseDTO addCourse(CourseDTO courseDTO);
    CourseDTO read(Long courseId);
    List<CourseDTO>readByAttribute(CourseAttribute courseAttribute);
    CourseDTO readReview(Long courseId);
    CourseDTO updateCourse(CourseDTO courseDTO);
    void deleteCourse(Long courseId);
    List<CourseDTO> readAll();
    List<CourseDTO> getCoursesByNickname(String nickname);
    List<MemberCourseDTO> getMemberCoursesByMemberId(Long memberId);
    List<CourseDTO> getCoursesByMemberId(Long memberId);

}
