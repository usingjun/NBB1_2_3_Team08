package edu.example.learner.courseabout.course.repository;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.MemberCourse;
import edu.example.learner.courseabout.course.repository.search.MemberCourseSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberCourseRepository extends JpaRepository<MemberCourse, Long> , MemberCourseSearch {

    @Query("SELECT mc FROM MemberCourse mc WHERE mc.course.courseId = :courseId AND mc.member.memberId = :memberId")
    MemberCourse findByCourseIdAndMemberId(@Param("courseId") Long courseId, @Param("memberId") Long memberId);

    List<MemberCourse> findByMember_MemberId(Long memberId);

}
