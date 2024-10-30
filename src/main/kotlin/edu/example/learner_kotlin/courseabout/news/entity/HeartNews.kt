package edu.example.learner_kotlin.courseabout.news.entity

import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "heart")
class HeartNews protected constructor() {  // protected 생성자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_news_id")
    private val id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private lateinit var member: Member

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private lateinit var newsEntity: NewsEntity

    constructor(member: Member, newsEntity: NewsEntity) : this() {
        this.member = member
        this.newsEntity = newsEntity
    }
}