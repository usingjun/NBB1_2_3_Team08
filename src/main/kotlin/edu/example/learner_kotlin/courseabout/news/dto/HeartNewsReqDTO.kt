package edu.example.learner_kotlin.courseabout.news.dto

import jakarta.validation.constraints.NotNull

data class HeartNewsReqDTO(
    @field:NotNull(message = "Member must not be null")
    val memberId: Long,

    @field:NotNull(message = "News must not be null")
    val newsId: Long
)