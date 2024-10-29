package edu.example.learner.courseabout.course.repository.search;

import edu.example.learner.courseabout.course.entity.MemberCourse;

import java.util.List;

public interface MemberCourseSearch {
    List<MemberCourse> getMemberCourse(Long memberId);
}
