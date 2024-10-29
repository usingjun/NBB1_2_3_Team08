package edu.example.learner.courseabout.news.repository;

import edu.example.learner.courseabout.news.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @Query("SELECT n FROM NewsEntity n WHERE n.courseNews.courseId = :courseId")
    Page<NewsEntity> findAllNewsByCourse(@Param("courseId") Long courseId, Pageable pageable);

    @Modifying
    @Query("update NewsEntity n set n.viewCount = n.viewCount + 1 where n.newsId = :newsId")
    int updateView(@Param("newsId") Long newsId);

    @Modifying
    @Query("update NewsEntity n set n.likeCount = n.likeCount + 1 where n = :news")
    void addLikeCount(@Param("news") NewsEntity news);

    @Modifying
    @Query("update NewsEntity n set n.likeCount = n.likeCount - 1 where n = :news")
    void subLikeCount(@Param("news") NewsEntity news);
}
