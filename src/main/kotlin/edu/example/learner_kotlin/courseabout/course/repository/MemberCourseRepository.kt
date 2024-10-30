package edu.example.learner_kotlin.courseabout.course.repository


import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import edu.example.learner_kotlin.courseabout.course.repository.search.MemberCourseSearch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberCourseRepository : JpaRepository<MemberCourse, Long>, MemberCourseSearch {
    fun findByMember_MemberId(memberId: Long?): List<MemberCourse?>?
    @Query("SELECT mc FROM MemberCourse mc WHERE mc.course.courseId = :courseId AND mc.member.memberId= :memberId")
    fun findByCourseIdAndMember_Id(courseId: Long, memberId: Long): MemberCourse

}