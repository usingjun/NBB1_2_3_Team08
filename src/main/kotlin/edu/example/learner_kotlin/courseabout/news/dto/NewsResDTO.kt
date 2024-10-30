package edu.example.learner_kotlin.courseabout.news.dto

import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import java.time.LocalDateTime

data class NewsResDTO(
    val newsId: Long? = null,
    val newsName: String? = null,
    val newsDescription: String? = null,
    val newsDate: LocalDateTime? = null,
    val lastModifiedDate: LocalDateTime? = null,
    val viewCount: Int = 0,
    val likeCount: Int = 0
) {
    companion object {
        fun fromEntity(entity: NewsEntity): NewsResDTO {
            return NewsResDTO(
                newsId = entity.newsId,
                newsName = entity.newsName,
                newsDescription = entity.newsDescription,
                newsDate = entity.newsDate,
                lastModifiedDate = entity.lastModifiedDate,
                viewCount = entity.viewCount,
                likeCount = entity.likeCount
            )
        }
    }
}