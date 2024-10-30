package edu.example.learner_kotlin.courseabout.course.repository

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface CourseRepository : JpaRepository<Course, Long> {
    fun readByCourseAttribute(courseAttribute: CourseAttribute): List<Course?>
    fun getByMemberNickname(memberNickname: String): List<Course>
//    fun readByCourseMember(memberNickname: String): List<Course?>

    @EntityGraph(attributePaths = ["member"])
    @Query("SELECT c FROM Course c WHERE c.courseId = :courseId")
    fun findByIdWithMember(@Param("courseId") courseId: Long): Optional<Course>
}
