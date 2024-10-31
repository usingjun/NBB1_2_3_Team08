package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CourseAnswerRepository : JpaRepository<CourseAnswer, Long> {
    @Query("SELECT ca FROM CourseAnswer ca WHERE ca.courseInquiry.inquiryId = :inquiryId")
    fun getCourseAnswers(inquiryId: Long): List<CourseAnswer>?
}