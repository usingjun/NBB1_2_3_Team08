package edu.example.learner.courseabout.repository;

import edu.example.learner.alarm.entity.Alarm;
import edu.example.learner.alarm.entity.AlarmType;
import edu.example.learner.alarm.entity.Priority;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import edu.example.learner.courseabout.course.repository.CourseRepository;
import edu.example.learner.courseabout.coursereview.dto.ReviewDTO;
import edu.example.learner.courseabout.coursereview.entity.Review;
import edu.example.learner.courseabout.coursereview.entity.ReviewType;
import edu.example.learner.courseabout.coursereview.repository.ReviewRepository;
import edu.example.learner.courseabout.exception.ReviewException;
import edu.example.learner.member.entity.Member;
import edu.example.learner.member.entity.Role;
import edu.example.learner.member.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .nickname("test")
                .role(Role.USER)
                .email("test@example.com")
                .password("test")
                .phoneNumber("010-1111-1111")
                .introduction("테스트")
                .build();
        memberRepository.save(member);

        Course course = Course.builder()
                .courseAttribute(CourseAttribute.C)
                .courseDescription("테스트")
                .courseLevel(3)
                .coursePrice(10000L)
                .courseName("test")
                .sale(true)
                .build();
        courseRepository.save(course);


        // 여러 개의 리뷰 추가
        for (int i = 1; i <= 3; i++) {
            Review review = Review.builder()
                    .reviewName("리뷰" + i)
                    .reviewDetail("리뷰 내용")
                    .rating(3)
                    .reviewType(ReviewType.COURSE)
                    .member(member)
                    .course(course)
                    .build();

            Review savedReview = reviewRepository.save(review);
        }


    }

    @Test
    @Order(1)
    @Rollback(false)
    public void testInsert() {
        Long mId = 1L;
        Long cId = 1L;

        Member member = Member.builder().memberId(mId).build();
        Course course = Course.builder().courseId(cId).build();



        //GIVEN
            Review review = Review.builder()
                    .reviewName("리뷰")
                    .reviewDetail("리뷰 내용")
                    .rating(3)
                    .reviewType(ReviewType.COURSE)
                    .member(member)
                    .course(course)
                    .build();

            Review savedReview = reviewRepository.save(review);
            assertNotNull(savedReview);
//            assertEquals(4,savedReview.getReviewId());
    }

    @Test
    @Order(2)
    public void testRead() {
        Long reviewId = 1L;

        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        assertTrue(foundReview.isPresent(), "Review should be presented");

        System.out.println("------------");

        Course course = foundReview.get().getCourse();
        Member member = foundReview.get().getMember();

        assertNotNull(course);
        assertEquals(1, course.getCourseId());

        assertNotNull(member);
        assertEquals(1, member.getMemberId());
    }

    @Test
    @Order(3)
    public void testUpdate() {
        Long reviewId = 1L;
        String content = "리뷰 수정 테스트";
        String detail = "리뷰 수정 테스트 내용";
        int star = 1;

        Optional<Review> foundReview = reviewRepository.findById(reviewId);
        assertTrue(foundReview.isPresent(), "Review should be presented");

        Review review = foundReview.get();
        review.changeReviewName(content);
        review.changeReviewDetail(detail);
        review.changeRating(star);

        Review updatedReview = reviewRepository.findById(reviewId).get();
        assertEquals(content, updatedReview.getReviewName());
        assertEquals(detail, updatedReview.getReviewDetail());
        assertEquals(star, updatedReview.getRating());
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        Long reviewID = 4L;

        assertTrue(reviewRepository.findById(reviewID).isPresent(), "foundReview should be present");

        reviewRepository.deleteById(reviewID);

        assertFalse(reviewRepository.findById(reviewID).isPresent(), "foundReview should not be present");
    }

}