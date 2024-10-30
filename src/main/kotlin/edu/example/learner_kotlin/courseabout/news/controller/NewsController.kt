package edu.example.learner_kotlin.courseabout.news.controller

import edu.example.learner_kotlin.courseabout.news.dto.HeartNewsReqDTO
import edu.example.learner_kotlin.courseabout.news.dto.NewsResDTO
import edu.example.learner_kotlin.courseabout.news.dto.NewsRqDTO
import edu.example.learner_kotlin.courseabout.news.service.HeartNewsService
import edu.example.learner_kotlin.courseabout.news.service.NewsService
import edu.example.learner_kotlin.redis.RedisServiceImpl
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Pageable
import java.time.Duration

@RestController
@RequestMapping("/course/{courseId}/news")
//@Tag(name = "News", description = "새소식 API")
class NewsController(
    private val newsService: NewsService,
    private val heartNewsService: HeartNewsService,
    private val redisViewServiceImpl: RedisServiceImpl
) {

    @PostMapping
//    @Operation(summary = "새소식 등록", description = "새소식을 등록합니다.")
    fun createNews(@PathVariable courseId: Long, @Validated @RequestBody newsRqDTO: NewsRqDTO): ResponseEntity<NewsResDTO> {
        val createdNews = newsService.createNews(courseId, newsRqDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews)
    }

    @PutMapping("/{newsId}")
//    @Operation(summary = "새소식 수정", description = "새소식을 수정합니다.")
    fun updateNews(@PathVariable courseId: Long, @PathVariable newsId: Long, @Validated @RequestBody newsRqDTO: NewsRqDTO): ResponseEntity<NewsResDTO> {
        val updatedNews = newsService.updateNews(courseId, newsId, newsRqDTO)
        return ResponseEntity.ok().body(updatedNews)
    }

    @DeleteMapping("/{newsId}")
//    @Operation(summary = "새소식 삭제", description = "새소식을 삭제합니다.")
    fun deleteNews(@PathVariable courseId: Long, @PathVariable newsId: Long): ResponseEntity<Any> {
        newsService.deleteNews(courseId, newsId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{newsId}")
//    @Operation(summary = "특정 새소식 조회", description = "특정 새소식을 조회합니다.")
    fun getNews(@PathVariable courseId: Long, @PathVariable newsId: Long, request: HttpServletRequest): ResponseEntity<NewsResDTO> {
        val news = newsService.getNews(courseId, newsId)

        var ipAddress = request.getHeader("X-Forwarded-For") ?: request.remoteAddr
        if (ipAddress == "0:0:0:0:0:0:0:1") {
            ipAddress = "localhost" // 테스트용 IP
        }

        val redisKey = "viewNews:$newsId:-$ipAddress"
        if (!redisViewServiceImpl.isDuplicateView(redisKey, Duration.ofHours(24))) {
            newsService.addViewCountV2(newsId)
        }

        return ResponseEntity.ok().body(news)
    }

    @GetMapping
//    @Operation(summary = "전체 새소식 조회", description = "전체 새소식을 조회합니다.")
    fun getAllNews(
        @PathVariable courseId: Long,
        @PageableDefault(size = 8, direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<NewsResDTO>> {
        val allNews = newsService.getAllNews(courseId, pageable)
        return ResponseEntity.ok().body(allNews)
    }

    @PatchMapping("/{newsId}/like")
//    @Operation(summary = "좋아요", description = "좋아요를 처리합니다.")
    fun increaseHeart(@RequestBody @Validated heartNewsReqDTO: HeartNewsReqDTO): ResponseEntity<Any> {
        heartNewsService.addHeart(heartNewsReqDTO)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{newsId}/like")
//    @Operation(summary = "좋아요 취소", description = "좋아요를 취소합니다.")
    fun decreaseHeart(@RequestBody @Validated heartNewsReqDTO: HeartNewsReqDTO): ResponseEntity<Any> {
        heartNewsService.deleteHeart(heartNewsReqDTO)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{newsId}/like")
//    @Operation(summary = "좋아요 여부 확인", description = "좋아요 여부를 확인합니다.")
    fun checkHeart(@PathVariable newsId: Long, @RequestParam memberId: Long): ResponseEntity<Boolean> {
        val isHeart = heartNewsService.checkHeart(newsId, memberId)
        return ResponseEntity.ok().body(isHeart)
    }
}