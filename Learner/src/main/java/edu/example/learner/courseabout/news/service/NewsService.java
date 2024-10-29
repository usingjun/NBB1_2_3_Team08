package edu.example.learner.courseabout.news.service;

import edu.example.learner.courseabout.news.dto.NewsResDTO;
import edu.example.learner.courseabout.news.dto.NewsRqDTO;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.news.entity.NewsEntity;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.news.repository.NewsRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final CourseRepository courseRepository;

    public NewsResDTO createNews(Long courseId, NewsRqDTO newsRqDTO) {
        log.info("새소식 등록 : {}", newsRqDTO.getNewsName());
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));
        NewsEntity newsEntity = newsRqDTO.toEntity();
        newsEntity.changeCourse(course);
        newsRepository.save(newsEntity);

        return NewsResDTO.fromEntity(newsEntity);
    }

    public NewsResDTO updateNews(Long courseId, Long newsId, NewsRqDTO newsRqDTO) {
        log.info("새소식 업데이트 : {}", newsRqDTO.getNewsName());

        // 검증 메서드 호출
        NewsEntity newsEntity = validateNewsInCourse(courseId, newsId);
        //업데이트
        newsEntity.changeNewsName(newsRqDTO.getNewsName());
        newsEntity.changeNewsDescription(newsRqDTO.getNewsDescription());

        return NewsResDTO.fromEntity(newsRepository.save(newsEntity));
    }

    public void deleteNews(Long courseId, Long newsId) {
        log.info("새소식 삭제: {}", newsId);

        // 검증 메서드 호출
        NewsEntity newsEntity = validateNewsInCourse(courseId, newsId);

        newsRepository.deleteById(newsId);
    }

    @Transactional(readOnly = true)
    public NewsResDTO getNews(Long courseId, Long newsId) {
        log.info("새소식 조회 : {}", newsId);

        // 검증 메서드 호출
        NewsEntity newsEntity = validateNewsInCourse(courseId, newsId);

        return NewsResDTO.fromEntity(newsEntity);
    }

    @Transactional(readOnly = true)
    public Page<NewsResDTO> getAllNews(Long courseId, Pageable pageable) {
        log.info("전체 새소식 조회: 강의 ID {}", courseId);

        // 해당 강의에 속하는 모든 새소식 조회
        Page<NewsEntity> newsEntities = newsRepository.findAllNewsByCourse(courseId, pageable);

        return newsEntities.map(NewsResDTO::fromEntity);

    }

    private NewsEntity validateNewsInCourse(Long courseId, Long newsId) {
        NewsEntity newsEntity = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 소식을 찾을 수 없습니다."));

        if (!newsEntity.getCourseNews().getCourseId().equals(courseId)) {
            throw new IllegalArgumentException("해당 강의에 속하지 않는 소식입니다.");
        }

        return newsEntity;
    }

    // 쿠키 버전
    public void addViewCount(HttpServletRequest request, HttpServletResponse response, Long newsId) {

        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + newsId.toString() + "]")) {
                newsRepository.updateView(newsId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + newsId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 시간
                response.addCookie(oldCookie);
            }
        } else {
            newsRepository.updateView(newsId);
            Cookie newCookie = new Cookie("postView", "[" + newsId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); // 쿠키시간
            response.addCookie(newCookie);
        }
    }

    //  조회수 증가
    public void addViewCountV2(Long newsId) {
        NewsEntity newsEntity = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디의 소식을 찾을 수 없습니다."));

        newsEntity.changeViewCount(newsEntity.getViewCount() + 1);
        newsRepository.save(newsEntity);
    }


}
