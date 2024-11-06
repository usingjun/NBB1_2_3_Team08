package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.repository

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.InquiryStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface CourseInquiryRepository : JpaRepository<CourseInquiry, Long> {
    @Query("SELECT ci FROM CourseInquiry ci WHERE ci.course.courseId = :courseId")
    fun getCourseInquirys(@Param("courseId") courseId: Long): List<CourseInquiry>?

    @Query("""
        SELECT ci FROM CourseInquiry ci 
        WHERE ci.course.courseId = :courseId 
        ORDER BY 
            CASE 
                WHEN ci.inquiryStatus = 'PENDING' THEN 0
                WHEN ci.inquiryStatus = 'ANSWERED' THEN 1
                WHEN ci.inquiryStatus = 'RESOLVED' THEN 2
                ELSE 3
            END
    """)
    fun findAllOrderByStatus(@Param("courseId") courseId: Long): List<CourseInquiry>?
}