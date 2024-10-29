package edu.example.learner.courseabout.news.controller;

import edu.example.learner.courseabout.news.dto.HeartNewsReqDTO;
import edu.example.learner.courseabout.news.dto.NewsResDTO;
import edu.example.learner.courseabout.news.dto.NewsRqDTO;
import edu.example.learner.courseabout.news.service.HeartNewsService;
import edu.example.learner.courseabout.news.service.NewsService;
import edu.example.learner.redis.RedisServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course/{courseId}/news")
@Tag(name = "News", description = "새소식 API")
public class NewsController {
    private final NewsService newsService;
    private final HeartNewsService heartNewsService;
    private final RedisServiceImpl redisViewServiceImpl;

    // 새소식 등록
    @PostMapping
    @Operation(summary = "새소식 등록", description = "새소식을 등록합니다.")
    public ResponseEntity<NewsResDTO> createNews(@PathVariable Long courseId, @Validated @RequestBody NewsRqDTO newsRqDTO) {
        NewsResDTO createdNews = newsService.createNews(courseId, newsRqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    // 새소식 업데이트
    @PutMapping("/{newsId}")
    @Operation(summary = "새소식 수정", description = "새소식을 수정합니다.")
    public ResponseEntity<NewsResDTO> updateNews(@PathVariable Long courseId,
                                                 @PathVariable Long newsId,
                                                 @Validated @RequestBody NewsRqDTO newsRqDTO) {
        NewsResDTO updatedNews = newsService.updateNews(courseId, newsId, newsRqDTO);
        return ResponseEntity.ok().body(updatedNews);
    }

    // 새소식 삭제
    @DeleteMapping("/{newsId}")
    @Operation(summary = "새소식 삭제", description = "새소식을 삭제합니다.")
    public ResponseEntity<?> deleteNews(@PathVariable Long courseId,
                                        @PathVariable Long newsId) {
        newsService.deleteNews(courseId, newsId);
        return ResponseEntity.ok().build();
    }

    // 특정 새소식 조회
    @GetMapping("/{newsId}")
    @Operation(summary = "특정 새소식 조회", description = "특정 새소식을 조회합니다.")
    public ResponseEntity<NewsResDTO> getNews(@PathVariable Long courseId,
                                              @PathVariable Long newsId,
                                              HttpServletRequest request) {
        NewsResDTO news = newsService.getNews(courseId, newsId);

        //조회수 올리기
//        String ipAddress = request.getRemoteAddr(); // IP주소
        // IP 주소 확인 (리버스 프록시 고려)
        // IP 주소 확인 (리버스 프록시 고려)
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        // 로컬 개발 환경에서는 테스트를 위해 직접 IP를 설정
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "localhost"; // 테스트용 IP
        }

        String redisKey = "viewNews:" + newsId + ":-" + ipAddress;

        // 레디스 중복조회 여부확인
        boolean isDuplicate = redisViewServiceImpl.isDuplicateView(redisKey, Duration.ofHours(24));
        if (!isDuplicate) {
            // 중복이 아닐시 조회수 증가
            newsService.addViewCountV2(newsId);
        }


        return ResponseEntity.ok().body(news);
    }

    // 전체 새소식 조회
    @GetMapping
    @Operation(summary = "전체 새소식 조회", description = "전체 새소식을 조회합니다.")
    public ResponseEntity<Page<NewsResDTO>> getAllNews(@PathVariable Long courseId,
                                                       @PageableDefault(size = 8, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NewsResDTO> allNews = newsService.getAllNews(courseId, pageable);
        return ResponseEntity.ok().body(allNews);
    }

    //좋아요 처리
    // courseId 처리는 고려
    @PatchMapping("/{newsId}/like")
    @Operation(summary = "좋아요", description = "좋아요를 처리합니다.")
    public ResponseEntity<?> increaseHeart(@RequestBody @Validated HeartNewsReqDTO heartNewsReqDTO) throws Exception {
        heartNewsService.insert(heartNewsReqDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{newsId}/like")
    @Operation(summary = "좋아요 취소", description = "좋아요를 취소합니다.")
    public ResponseEntity<?> decreaseHeart(@RequestBody @Validated HeartNewsReqDTO heartNewsReqDTO) {
        heartNewsService.delete(heartNewsReqDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{newsId}/like")
    @Operation(summary = "좋아요 여부 확인", description = "좋아요 여부를 확인합니다.")
    public ResponseEntity<Boolean> checkHeart(@PathVariable Long newsId, @RequestParam Long memberId) {
        boolean isHeart = heartNewsService.checkHeart(newsId, memberId);
        return ResponseEntity.ok().body(isHeart);
    }
}
