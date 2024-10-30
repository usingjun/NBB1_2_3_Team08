package edu.example.learner_kotlin.courseabout.news.repository

import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface NewsRepository : JpaRepository<NewsEntity, Long> {
    @Query("SELECT n FROM NewsEntity n WHERE n.courseNews.courseId = :courseId")
    fun findAllNewsByCourse(@Param("courseId") courseId: Long, pageable: Pageable): Page<NewsEntity>

    @Modifying
    @Query("UPDATE NewsEntity n SET n.viewCount = n.viewCount + 1 WHERE n.newsId = :newsId")
    fun updateView(@Param("newsId") newsId: Long): Int

    @Modifying
    @Query("UPDATE NewsEntity n SET n.likeCount = n.likeCount + 1 WHERE n = :news")
    fun addLikeCount(@Param("news") news: NewsEntity)

    @Modifying
    @Query("UPDATE NewsEntity n SET n.likeCount = n.likeCount - 1 WHERE n = :news")
    fun subLikeCount(@Param("news") news: NewsEntity)
}