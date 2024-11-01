package edu.example.learner_kotlin.courseabout.course.service

import edu.example.learner_kotlin.courseabout.course.dto.CourseCreateDTO
import edu.example.learner_kotlin.courseabout.course.dto.CourseDTO
import edu.example.learner_kotlin.courseabout.course.dto.CourseUpdateDTO
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member

object CourseMapper {
    fun toEntity(dto:CourseCreateDTO, member: Member): Course{
        return Course().apply {
            courseName=dto.courseName
            courseDescription=dto.courseDescription
            courseLevel=dto.courseLevel
            courseAttribute = CourseAttribute.fromString(dto.courseAttribute)
            coursePrice=dto.coursePrice
            this.member =member
        }
    }

    fun toUpdateEntity(dto: CourseUpdateDTO, existingCourse: Course): Course{
        return existingCourse.apply {
            courseName=dto.courseName
            courseLevel=dto.courseLevel
            coursePrice=dto.coursePrice
            courseDescription=dto.courseDescription
            courseAttribute= CourseAttribute.fromString(dto.courseAttribute)
        }
    }
    fun toDTO(course: Course): CourseDTO {
        return CourseDTO(
            courseId = course.courseId,
            courseName = course.courseName,
            courseDescription = course.courseDescription,
            coursePrice = course.coursePrice,
            memberNickname = course.member?.nickname ?: "",
            courseLevel = course.courseLevel,
            courseAttribute = course.courseAttribute?.name,
            sale = course.sale,
            memberId = course.member?.memberId,
            createdAt = course.courseCreatedDate
        )
    }
}