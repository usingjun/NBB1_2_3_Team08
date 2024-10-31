package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface CourseInquiryRepository : JpaRepository<CourseInquiry, Long> {
    @Query("SELECT ci FROM CourseInquiry ci WHERE ci.course.courseId = :courseId")
    fun getCourseInquirys(@Param("courseId") courseId: Long): List<CourseInquiry>?
}