package edu.example.learner_kotlin.courseabout.news.service

import edu.example.learner_kotlin.courseabout.news.dto.NewsResDTO
import edu.example.learner_kotlin.courseabout.news.dto.NewsRqDTO
import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import edu.example.learner_kotlin.courseabout.news.repository.NewsRepository
import edu.example.learner_kotlin.log
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class NewsService(
    private val newsRepository: NewsRepository,
    private val courseRepository: CourseRepository
) {
    fun createNews(courseId: Long, newsRqDTO: NewsRqDTO): NewsResDTO {
        log.info("새소식 등록 : ${newsRqDTO.newsName}")
        // 반환 타입이 Optional<Course>이므로 orElseThrow를 사용하여 예외처리
        // :? 사용하니 unreachable code
        val course = courseRepository.findById(courseId)
            .orElseThrow { IllegalArgumentException("해당 강의를 찾을 수 없습니다.") }
        val newsEntity = newsRqDTO.toEntity().apply { changeCourse(course) }
        newsRepository.save(newsEntity)
        return NewsResDTO.fromEntity(newsEntity)
    }

    fun updateNews(courseId: Long, newsId: Long, newsRqDTO: NewsRqDTO): NewsResDTO {
        log.info("새소식 업데이트 : ${newsRqDTO.newsName}")
        val newsEntity = validateNewsInCourse(courseId, newsId)
        newsEntity.changeNewsName(newsRqDTO.newsName)
        newsEntity.changeNewsDescription(newsRqDTO.newsDescription)
        return NewsResDTO.fromEntity(newsRepository.save(newsEntity))
    }

    fun deleteNews(courseId: Long, newsId: Long) {
        log.info("새소식 삭제 : $newsId")
        val newsEntity = validateNewsInCourse(courseId, newsId)
        newsRepository.deleteById(newsEntity.newsId!!)
    }

    @Transactional(readOnly = true)
    fun getNews(courseId: Long, newsId: Long): NewsResDTO {
        log.info("새소식 조회 : $newsId")
        val newsEntity = validateNewsInCourse(courseId, newsId)
        return NewsResDTO.fromEntity(newsEntity)
    }

    @Transactional(readOnly = true)
    fun getAllNews(courseId: Long, pageable: Pageable): Page<NewsResDTO> {
        log.info("전체 새소식 조회: 강의 ID $courseId")
        val newsEntities = newsRepository.findAllNewsByCourse(courseId, pageable)
        return newsEntities.map(NewsResDTO::fromEntity)
    }

    private fun validateNewsInCourse(courseId: Long, newsId: Long): NewsEntity {
        val newsEntity = newsRepository.findById(newsId)
            .orElseThrow { IllegalArgumentException("해당 아이디의 소식을 찾을 수 없습니다.") }
        if (newsEntity.courseNews?.courseId != courseId) {
            throw IllegalArgumentException("해당 강의에 속하지 않는 소식입니다.")
        }
        return newsEntity
    }

    // 쿠키 버전 조회수 증가
    fun addViewCount(request: HttpServletRequest, response: HttpServletResponse, newsId: Long) {
        var oldCookie: Cookie? = null
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "postView") {
                    oldCookie = cookie
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.value.contains("[$newsId]")) {
                newsRepository.updateView(newsId)
                oldCookie.value = oldCookie.value + "_[$newsId]"
                oldCookie.path = "/"
                oldCookie.maxAge = 60 * 60 * 24 // 쿠키 시간 설정
                response.addCookie(oldCookie)
            }
        } else {
            newsRepository.updateView(newsId)
            val newCookie = Cookie("postView", "[$newsId]").apply {
                path = "/"
                maxAge = 60 * 60 * 24 // 쿠키 시간 설정
            }
            response.addCookie(newCookie)
        }
    }

    // 조회수 증가
    fun addViewCountV2(newsId: Long) {
        val newsEntity = newsRepository.findById(newsId)
            .orElseThrow { IllegalArgumentException("해당 아이디의 소식을 찾을 수 없습니다.") }
        newsEntity.changeViewCount(newsEntity.viewCount + 1)
        newsRepository.save(newsEntity)
    }

}
