package edu.example.learner_kotlin.courseabout.course.dto

import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute

data class CourseCreateDTO (
    val courseDescription: String,
    val courseName: String,
    val memberNickname: String,
    val courseLevel: Int,
    val courseAttribute: String,
    val coursePrice: Long,
    val isSale: Boolean,
){
}