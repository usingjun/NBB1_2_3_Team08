package edu.example.learner.courseabout.service;

import edu.example.learner.courseabout.news.dto.HeartNewsReqDTO;
import edu.example.learner.courseabout.news.dto.NewsRqDTO;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.news.entity.HeartNews;
import edu.example.learner.courseabout.news.entity.NewsEntity;
import edu.example.learner.courseabout.news.service.HeartNewsService;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.news.repository.HeartNewsRepository;
import edu.example.learner.courseabout.news.repository.NewsRepository;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import edu.example.learner.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
@Slf4j
class HeartNewsServiceTest {

    @Autowired
    private HeartNewsService heartNewsService;
    @Autowired
    private HeartNewsRepository heartNewsRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CourseRepository courseRepository;
    private Member member;
    private NewsEntity news;
    private Course savedCourse;

    @BeforeEach
    public void setUp() {
        // Create a member
        member = Member.builder()
                .memberId(1L)
                .email("test@example.com")
                .password("password123")
                .nickname("nickname")
                .phoneNumber("010-1234-5678")
                .profileImage(null) // 필요 시 이미지 경로를 추가
                .role(Role.ADMIN)
                .introduction("Hi, I'm a test user.")
                .createDate(LocalDateTime.now())
                .build();
        memberRepository.save(member);
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
        news = newsRepository.save(newsRqDTO.toEntity());
        news.changeCourse(savedCourse);  // 강의와 연결
    }

    @Test
    public void testInsertHeartNews() throws Exception {
        // Given
        HeartNewsReqDTO heartNewsReqDTO = new HeartNewsReqDTO(member.getMemberId(), news.getNewsId());

        // When
        heartNewsService.insert(heartNewsReqDTO);

        // Then
        Optional<HeartNews> heartNews = heartNewsRepository.findByMemberAndNewsEntity(member, news);
        assertTrue(heartNews.isPresent());
    }

    @Test
    public void testDeleteHeartNews() throws Exception {
        // Given
        HeartNewsReqDTO heartNewsReqDTO = new HeartNewsReqDTO(member.getMemberId(), news.getNewsId());

        // 좋아요 등록
        heartNewsService.insert(heartNewsReqDTO);

        // When
        heartNewsService.delete(heartNewsReqDTO);

        // Then
        Optional<HeartNews> heartNews = heartNewsRepository.findByMemberAndNewsEntity(member, news);
        assertFalse(heartNews.isPresent());
        assertEquals(0, news.getLikeCount());
    }
}