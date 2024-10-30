package edu.example.learner_kotlin.courseabout.news.repository

import edu.example.learner_kotlin.courseabout.news.entity.HeartNews
import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface HeartNewsRepository : JpaRepository<HeartNews, Long> {
    fun findByMemberAndNewsEntity(member: Member, newsEntity: NewsEntity): HeartNews?

    fun existsByMemberAndNewsEntity(member: Member, newsEntity: NewsEntity): Boolean
}