package edu.example.learner_kotlin.courseabout.course.dto

import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import java.time.LocalDateTime

data class CourseUpdateDTO(
    var courseId : Long,
    val courseName: String,
    val courseDescription: String,
    val courseLevel: Int,
    val coursePrice: Long,
    val courseAttribute: String,
    var sale: Boolean,

    ) {

}