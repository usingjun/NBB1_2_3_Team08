package edu.example.learner_kotlin.courseabout.course.dto

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.member.entity.Member
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDateTime

data class CourseDTO(
    var courseId: Long? = null,
    var courseName: String? = null,
    var courseDescription: String? = null,
    var coursePrice: Long? = null,
    var memberNickname: String? = null,

    @field:Min(1)
    @field:Max(5)
    var courseLevel: Int? = null,

    var courseAttribute: String? = null,
    var sale: Boolean = false,
    var memberId: Long? = null,
    var createdAt: LocalDateTime? = null
) {
        fun toEntity(): Course {
            return Course(
                member = Member(nickname = memberNickname),
                courseDescription = courseDescription,
                courseName = courseName,
                coursePrice = coursePrice,
                courseLevel = courseLevel,
                courseAttribute = CourseAttribute.JAVA,
                courseCreatedDate = createdAt,
                sale = false
            )
        }


    constructor(course: Course) : this(
        courseId = course.courseId,
        courseName = course.courseName,
        courseDescription = course.courseDescription,
        coursePrice = course.coursePrice,
        memberNickname = course.member!!.nickname,
        courseLevel = course.courseLevel,
        courseAttribute = course.courseAttribute?.name,
        sale = course.sale,
        memberId = course.member!!.memberId,
        createdAt = course.courseCreatedDate
    )
}
