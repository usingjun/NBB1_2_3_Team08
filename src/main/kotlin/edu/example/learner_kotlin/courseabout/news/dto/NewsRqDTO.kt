package edu.example.learner_kotlin.courseabout.news.dto

import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import jakarta.validation.constraints.NotBlank

data class NewsRqDTO(
    @field:NotBlank(message = "새소식을 적어주세요.")
    val newsName: String,
    val newsDescription: String? = null
) {
    fun toEntity(): NewsEntity {
        return NewsEntity(
            newsName = newsName,
            newsDescription = newsDescription
        )
    }
}