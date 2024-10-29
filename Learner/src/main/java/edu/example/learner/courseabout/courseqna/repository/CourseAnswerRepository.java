package edu.example.learner.courseabout.courseqna.repository;

import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseAnswerRepository extends JpaRepository<CourseAnswer, Long> {
    @Query( "SELECT ca FROM CourseAnswer ca WHERE ca.courseInquiry.inquiryId = :inquiryId ")
    Optional<List<CourseAnswer>> getCourseAnswers(@Param("inquiryId") Long inquiryId);
}
