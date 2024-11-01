package edu.example.learner_kotlin.courseabout.video.dto

data class VideoCreateDTO(
    val title: String,
    val url: String,
    val description: String?,
    val courseId: Long
) {
}