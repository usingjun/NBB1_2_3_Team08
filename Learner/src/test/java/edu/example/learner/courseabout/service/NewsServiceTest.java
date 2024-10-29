package edu.example.learner.courseabout.service;

import edu.example.learner.courseabout.news.dto.NewsResDTO;
import edu.example.learner.courseabout.news.dto.NewsRqDTO;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.news.entity.NewsEntity;
import edu.example.learner.courseabout.news.service.NewsService;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.news.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
public class NewsServiceTest {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NewsService newsService;
    private NewsEntity initialNews;
    private Course savedCourse;

    @BeforeEach
    public void setUp() {
        // 테스트를 위한 초기 데이터 설정
        Course course = new Course();
        course.changeCourseName("초기 강의");
        course.changeCourseDescription("초기 강의 설명");
        course.changePrice(30000L);
        course.changeCourseLevel(1);
        course.changeSale(false);

        savedCourse = courseRepository.save(course);

        NewsRqDTO newsRqDTO = new NewsRqDTO();
        newsRqDTO.setNewsName("초기 소식 제목");
        newsRqDTO.setNewsDescription("초기 소식 내용");

        // 뉴스 엔티티 등록
        initialNews = newsRepository.save(newsRqDTO.toEntity());
        initialNews.changeCourse(savedCourse);  // 강의와 연결
    }

    @Test
    public void registerNews() {
        // Given
        Course course = new Course();
        course.changeCourseName("새 강의");
        course.changeCourseDescription("새 강의 설명");
        course.changePrice(20000L);
        course.changeCourseLevel(2);
        course.changeSale(true);

        Course saveCourse = courseRepository.save(course);

        NewsRqDTO newsRqDTO = new NewsRqDTO();
        newsRqDTO.setNewsName("새소식 제목");
        newsRqDTO.setNewsDescription("새소식 내용");

        // When
        NewsResDTO newsResDTO = newsService.createNews(saveCourse.getCourseId(), newsRqDTO);

        // Then
        assertThat(newsResDTO).isNotNull();
        assertThat(newsResDTO.getNewsName()).isEqualTo("새소식 제목");
    }

    @Test
    public void updateNewsTest() {
        // Given
        Long newsId = initialNews.getNewsId();
        NewsRqDTO newsRqDTO = new NewsRqDTO();
        newsRqDTO.setNewsName("업데이트된 소식 제목");
        newsRqDTO.setNewsDescription("업데이트된 소식 내용");

        // When
        NewsResDTO updatedNewsResDTO = newsService.updateNews(savedCourse.getCourseId(), newsId, newsRqDTO);

        // Then
        assertThat(updatedNewsResDTO).isNotNull();
        assertThat(updatedNewsResDTO.getNewsName()).isEqualTo("업데이트된 소식 제목");
    }

    @Test
    public void deleteNewsTest() {
        // Given
        Long newsId = initialNews.getNewsId();

        // When
        newsService.deleteNews(savedCourse.getCourseId(), newsId);

        // Then
        assertThat(newsRepository.findById(newsId)).isEmpty();
    }

    @Test
    public void getNews() {
        // When
        NewsResDTO foundNews = newsService.getNews(savedCourse.getCourseId(), initialNews.getNewsId());

        log.info("출력 {}", foundNews);

        // Then
        assertThat(foundNews).isNotNull();
        assertThat(foundNews.getNewsName()).isEqualTo("초기 소식 제목");
        assertThat(foundNews.getViewCount()).isEqualTo(0);
    }

    @Test
    public void getAllNews() {
        // Given
        Course course = new Course();
        course.changeCourseName("테스트 강의");
        course.changeCourseDescription("테스트 강의 설명");
        course.changePrice(10000L);
        course.changeCourseLevel(1);
        course.changeSale(true);

        Course savedCourse = courseRepository.save(course);

        NewsRqDTO newsRqDTO1 = new NewsRqDTO();
        newsRqDTO1.setNewsName("새소식 1");
        newsRqDTO1.setNewsDescription("내용 1");
        NewsEntity news1 = newsRqDTO1.toEntity();
        news1.changeCourse(savedCourse); // 강의와 연결
        newsRepository.save(news1);

        NewsRqDTO newsRqDTO2 = new NewsRqDTO();
        newsRqDTO2.setNewsName("새소식 2");
        newsRqDTO2.setNewsDescription("내용 2");
        NewsEntity news2 = newsRqDTO2.toEntity();
        news2.changeCourse(savedCourse); // 강의와 연결
        newsRepository.save(news2);

        // Pageable 설정
        Pageable pageable = PageRequest.of(0, 10); // 0 페이지, 10개 항목

        // When
        Page<NewsResDTO> allNewsPage = newsService.getAllNews(savedCourse.getCourseId(), pageable);

        // Then
        assertThat(allNewsPage).isNotEmpty();
        assertThat(allNewsPage.getTotalElements()).isGreaterThan(1); // 초기 소식 포함
        assertThat(allNewsPage.getContent().size()).isLessThanOrEqualTo(10); // 페이지당 최대 10개 항목
    }

}