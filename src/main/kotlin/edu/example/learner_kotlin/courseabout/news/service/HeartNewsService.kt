package edu.example.learner_kotlin.courseabout.news.service

import edu.example.learner_kotlin.courseabout.exception.HeartNewsAlreadyExistsException
import edu.example.learner_kotlin.courseabout.exception.NotFoundException
import edu.example.learner_kotlin.courseabout.news.dto.HeartNewsReqDTO
import edu.example.learner_kotlin.courseabout.news.entity.HeartNews
import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import edu.example.learner_kotlin.courseabout.news.repository.HeartNewsRepository
import edu.example.learner_kotlin.courseabout.news.repository.NewsRepository
import edu.example.learner_kotlin.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HeartNewsService(
    private val newsRepository: NewsRepository,
    private val heartNewsRepository: HeartNewsRepository,
    private val memberRepository: MemberRepository
) {

    fun addHeart(heartNewsReqDTO: HeartNewsReqDTO) {
        val (member, news) = findMemberAndNews(heartNewsReqDTO.memberId, heartNewsReqDTO.newsId)

        // 이미 좋아요 되어있으면 에러 반환
        if (heartNewsRepository.existsByMemberAndNewsEntity(member, news)) {
            throw HeartNewsAlreadyExistsException("이미 좋아요가 등록되어 있습니다.")
        }

        val heartNews = HeartNews(
            newsEntity = news,
            member = member
        )

        heartNewsRepository.save(heartNews)
        newsRepository.addLikeCount(news)
    }

    fun deleteHeart(heartNewsReqDTO: HeartNewsReqDTO) {
        val (member, news) = findMemberAndNews(heartNewsReqDTO.memberId, heartNewsReqDTO.newsId)

        val heartNews = heartNewsRepository.findByMemberAndNewsEntity(member, news)
            ?: throw NotFoundException("삭제할 좋아요가 없습니다.")

        heartNewsRepository.delete(heartNews)
        newsRepository.subLikeCount(news)
    }

    fun checkHeart(newsId: Long, memberId: Long): Boolean {
        val (member, news) = findMemberAndNews(memberId, newsId)
        return heartNewsRepository.existsByMemberAndNewsEntity(member, news)
    }

    private fun findMemberAndNews(memberId: Long, newsId: Long): Pair<Member, NewsEntity> {
        val member = memberRepository.findById(memberId)
            .orElseThrow { NotFoundException("멤버아이디를 찾을 수 없습니다.") }
        val news = newsRepository.findById(newsId)
            .orElseThrow { NotFoundException("새소식을 찾을 수 없습니다.") }
        return Pair(member, news)
    }
}