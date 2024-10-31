package edu.example.learner_kotlin.courseabout.news.entity

import edu.example.learner_kotlin.courseabout.course.entity.Course
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "news")
data class NewsEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    val newsId: Long? = null,

    @Column(nullable = false)
    var newsName: String = "",

    var newsDescription: String? = null,

    @Column(columnDefinition = "integer default 0", nullable = false) // 조회수의 기본 값을 0으로 지정, null 불가 처리
    var viewCount: Int = 0,

    @Column(columnDefinition = "integer default 0", nullable = false)
    var likeCount: Int = 0,

    @CreatedDate
    var newsDate: LocalDateTime? = null,

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var courseNews: Course? = null,

)
{
    @OneToMany(mappedBy = "newsEntity", cascade = [CascadeType.ALL], orphanRemoval = true)
    val heartNewsList: List<HeartNews> = mutableListOf()

    fun changeCourse(course: Course) {
        this.courseNews = course
        course.newsEntities.add(this)
    }
}
