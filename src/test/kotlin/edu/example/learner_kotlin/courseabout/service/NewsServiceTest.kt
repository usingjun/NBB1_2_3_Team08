//package edu.example.learner_kotlin.courseabout.service
//
//import edu.example.learner_kotlin.courseabout.news.dto.NewsResDTO
//import edu.example.learner_kotlin.courseabout.news.dto.NewsRqDTO
//import edu.example.learner_kotlin.courseabout.course.entity.Course
//import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
//import edu.example.learner_kotlin.courseabout.news.service.NewsService
//import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
//import edu.example.learner_kotlin.courseabout.news.repository.NewsRepository
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.domain.Page
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.domain.Pageable
//import org.springframework.test.annotation.Rollback
//import org.springframework.transaction.annotation.Transactional
//import org.slf4j.LoggerFactory
//
//@SpringBootTest
//@Transactional
////@Rollback(false)
//class NewsServiceTest {
//
//    @Autowired
//    private lateinit var newsRepository: NewsRepository
//    @Autowired
//    private lateinit var courseRepository: CourseRepository
//    @Autowired
//    private lateinit var newsService: NewsService
//    private lateinit var initialNews: NewsEntity
//    private lateinit var savedCourse: Course
//
//    private val log = LoggerFactory.getLogger(this::class.java)
//
//    @BeforeEach
//    fun setUp() {
//        // 테스트를 위한 초기 데이터 설정
//        val course = Course().apply {
//            changeCourseName("초기 강의")
//            changeCourseDescription("초기 강의 설명")
//            changePrice(30000L)
//            changeCourseLevel(1)
//            changeSale(false)
//        }
//
//        savedCourse = courseRepository.save(course)
//
//        val newsRqDTO = NewsRqDTO().apply {
//            newsName = "초기 소식 제목"
//            newsDescription = "초기 소식 내용"
//        }
//
//        // 뉴스 엔티티 등록
//        initialNews = newsRepository.save(newsRqDTO.toEntity()).apply {
//            changeCourse(savedCourse)  // 강의와 연결
//        }
//    }
//
//    @Test
//    fun registerNews() {
//        // Given
//        val course = Course().apply {
//            changeCourseName("새 강의")
//            changeCourseDescription("새 강의 설명")
//            changePrice(20000L)
//            changeCourseLevel(2)
//            changeSale(true)
//        }
//
//        val savedCourse = courseRepository.save(course)
//
//        val newsRqDTO = NewsRqDTO().apply {
//            newsName = "새소식 제목"
//            newsDescription = "새소식 내용"
//        }
//
//        // When
//        val newsResDTO = newsService.createNews(savedCourse.courseId!!, newsRqDTO)
//
//        // Then
//        assertThat(newsResDTO).isNotNull
//        assertThat(newsResDTO.newsName).isEqualTo("새소식 제목")
//    }
//
//    @Test
//    fun updateNewsTest() {
//        // Given
//        val newsId = initialNews.newsId!!
//        val newsRqDTO = NewsRqDTO().apply {
//            newsName = "업데이트된 소식 제목"
//            newsDescription = "업데이트된 소식 내용"
//        }
//
//        // When
//        val updatedNewsResDTO = newsService.updateNews(savedCourse.courseId!!, newsId, newsRqDTO)
//
//        // Then
//        assertThat(updatedNewsResDTO).isNotNull
//        assertThat(updatedNewsResDTO.newsName).isEqualTo("업데이트된 소식 제목")
//    }
//
//    @Test
//    fun deleteNewsTest() {
//        // Given
//        val newsId = initialNews.newsId!!
//
//        // When
//        newsService.deleteNews(savedCourse.courseId!!, newsId)
//
//        // Then
//        assertThat(newsRepository.findById(newsId)).isEmpty
//    }
//
//    @Test
//    fun getNews() {
//        // When
//        val foundNews = newsService.getNews(savedCourse.courseId!!, initialNews.newsId!!)
//
//        log.info("출력 {}", foundNews)
//
//        // Then
//        assertThat(foundNews).isNotNull
//        assertThat(foundNews.newsName).isEqualTo("초기 소식 제목")
//        assertThat(foundNews.viewCount).isEqualTo(0)
//    }
//
//    @Test
//    fun getAllNews() {
//        // Given
//        val course = Course().apply {
//            changeCourseName("테스트 강의")
//            changeCourseDescription("테스트 강의 설명")
//            changePrice(10000L)
//            changeCourseLevel(1)
//            changeSale(true)
//        }
//
//        val savedCourse = courseRepository.save(course)
//
//        val newsRqDTO1 = NewsRqDTO().apply {
//            newsName = "새소식 1"
//            newsDescription = "내용 1"
//        }
//        val news1 = newsRqDTO1.toEntity().apply { changeCourse(savedCourse) }
//        newsRepository.save(news1)
//
//        val newsRqDTO2 = NewsRqDTO().apply {
//            newsName = "새소식 2"
//            newsDescription = "내용 2"
//        }
//        val news2 = newsRqDTO2.toEntity().apply { changeCourse(savedCourse) }
//        newsRepository.save(news2)
//
//        // Pageable 설정
//        val pageable: Pageable = PageRequest.of(0, 10)
//
//        // When
//        val allNewsPage: Page<NewsResDTO> = newsService.getAllNews(savedCourse.courseId!!, pageable)
//
//        // Then
//        assertThat(allNewsPage).isNotEmpty
//        assertThat(allNewsPage.totalElements).isGreaterThan(1)
//        assertThat(allNewsPage.content.size).isLessThanOrEqualTo(10)
//    }
//}
