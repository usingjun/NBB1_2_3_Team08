package edu.example.learner.courseabout.courseqna.repository;

import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseInquiryRepository extends JpaRepository<CourseInquiry, Long> {
    @Query( "SELECT ci FROM CourseInquiry ci WHERE ci.course.courseId = :courseId ")
    Optional<List<CourseInquiry>> getCourseInquirys(@Param("courseId") Long courseId);
}
