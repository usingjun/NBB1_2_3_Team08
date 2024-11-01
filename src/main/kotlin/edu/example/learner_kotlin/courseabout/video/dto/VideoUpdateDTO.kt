package edu.example.learner_kotlin.courseabout.video.dto

data class VideoUpdateDTO(
    val title: String,
    val url: String,
    val description: String?
) {
}