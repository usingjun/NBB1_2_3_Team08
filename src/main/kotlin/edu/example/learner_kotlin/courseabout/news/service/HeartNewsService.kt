package edu.example.learner_kotlin.courseabout.news.service

import edu.example.learner_kotlin.courseabout.exception.HeartNewsAlreadyExistsException
import edu.example.learner_kotlin.courseabout.exception.NotFoundException
import edu.example.learner_kotlin.courseabout.news.dto.HeartNewsReqDTO
import edu.example.learner_kotlin.courseabout.news.entity.HeartNews
import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import edu.example.learner_kotlin.courseabout.news.repository.HeartNewsRepository
import edu.example.learner_kotlin.courseabout.news.repository.NewsRepository
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HeartNewsService(
    private val newsRepository: NewsRepository,
    private val heartNewsRepository: HeartNewsRepository,
    private val memberRepository: MemberRepository
) {


    @Throws(Exception::class)
    fun insert(heartNewsReqDTO: HeartNewsReqDTO) {
        val member = memberRepository.findByIdOrNull(heartNewsReqDTO.memberId)
            ?: throw NotFoundException("멤버아이디를 찾을 수 없습니다.")

        val news = newsRepository.findByIdOrNull(heartNewsReqDTO.newsId)
            ?: throw NotFoundException("새소식을 찾을 수 없습니다.")

        // 이미 좋아요가 등록되어 있으면 예외 발생
        if (heartNewsRepository.findByMemberAndNewsEntity(member, news) != null) {
            throw HeartNewsAlreadyExistsException("이미 좋아요가 등록되어 있습니다.")
        }

        val heartNews = HeartNews(
            newsEntity = news,
            member = member
        )

        heartNewsRepository.save(heartNews)
        newsRepository.addLikeCount(news)
    }

    fun delete(heartNewsReqDTO: HeartNewsReqDTO) {
        val member = memberRepository.findByIdOrNull(heartNewsReqDTO.memberId)
            ?: throw NotFoundException("멤버아이디를 찾을 수 없습니다.")

        val news = newsRepository.findByIdOrNull(heartNewsReqDTO.newsId)
            ?: throw NotFoundException("새소식을 찾을 수 없습니다.")

        val heartNews = heartNewsRepository.findByMemberAndNewsEntity(member, news)
            ?: throw  NotFoundException("삭제할 좋아요가 없습니다.")

        heartNewsRepository.delete(heartNews)
        newsRepository.subLikeCount(news)
    }

    fun checkHeart(newsId: Long, memberId: Long): Boolean {
        val news = newsRepository.findById(newsId)
            .orElseThrow { NotFoundException("새소식을 찾을 수 없습니다.") }

        val member = memberRepository.findById(memberId)
            .orElseThrow { NotFoundException("멤버아이디를 찾을 수 없습니다.") }

        return heartNewsRepository.findByMemberAndNewsEntity(member, news) != null
    }
}
