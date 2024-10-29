package edu.example.learner.courseabout.course.repository;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> readByCourseAttribute(CourseAttribute courseAttribute);
    List<Course> getByMemberNickname(String memberNickname);

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT c FROM Course c WHERE c.courseId = :courseId")
    Optional<Course> findByIdWithMember(@Param("courseId") Long courseId);

}
