package edu.example.learner_kotlin.courseabout.repository

import edu.example.learner.courseabout.course.entity.Course
import edu.example.learner.courseabout.coursereview.entity.Review
import edu.example.learner.courseabout.coursereview.entity.ReviewType
import edu.example.learner.courseabout.coursereview.repository.ReviewRepository
import edu.example.learner_kotlin.courseabout.coursereview.exception.ReviewException
import edu.example.learner_kotlin.member.entity.Member
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import java.util.*
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ReviewRepositoryTest(
    private val reviewRepository: ReviewRepository? = null,
    private val memberRepository: MemberRepository? = null,
    private val courseRepository: CourseRepository? = null
) {


    @BeforeEach
    fun setUp() {
        val member: Member = Member().apply {
            nickname = "test"
//            role=Role.USER
            email = "test@example.com"
            password = "test"
            phoneNumber = "010-1111-1111"
            introduction = "테스트"
        }
        memberRepository.save(member)

        val course: Course = Course().apply {
            courseAttribute = CourseAttribute.C
            courseDescription = "테스트"
            courseLevel = 3
            coursePrice = 10000L
            courseName = "test"
            sale = true
        }
        courseRepository.save(course)


        // 여러 개의 리뷰 추가
        for (i in 1..3) {
            val review: Review = Review().apply {
                reviewName = "리뷰$i"
                reviewDetail = "리뷰 내용"
                rating = 3
                reviewType = ReviewType.COURSE
                member = member
                course = course
            }

            val savedReview: Review = reviewRepository?.save<Review>(review) ?: throw ReviewException.NOT_REGISTERED.get()
        }
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(1)
    @Rollback(false)
    fun testInsert() {
        val mId = 1L
        val cId = 1L

        val member: Member = Member().apply { memberId = mId }
        val course: Course = Course().apply { courseId = cId }


        //GIVEN
        val review: Review = Review().apply {
            reviewName="리뷰"
            reviewDetail="리뷰 내용"
            rating=3
            reviewType=ReviewType.COURSE
            member=member
            course=course
        }

        val savedReview: Review = reviewRepository?.save<Review>(review) ?: throw ReviewException.NOT_REGISTERED.get()
        org.junit.jupiter.api.Assertions.assertNotNull(savedReview)
        //            assertEquals(4,savedReview.getReviewId());
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(2)
    fun testRead() {
        val reviewId = 1L

        val foundReview: java.util.Optional<Review?> = reviewRepository!!.findById(reviewId)
        org.junit.jupiter.api.Assertions.assertTrue(foundReview.isPresent(), "Review should be presented")

        println("------------")

        val course: Course = foundReview.get().course
        val member: Member = foundReview.get().member

        org.junit.jupiter.api.Assertions.assertNotNull(course)
        assertEquals(1, course.getCourseId())

        org.junit.jupiter.api.Assertions.assertNotNull(member)
        assertEquals(1, member.getMemberId())
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(3)
    fun testUpdate() {
        val reviewId = 1L
        val content = "리뷰 수정 테스트"
        val detail = "리뷰 수정 테스트 내용"
        val star = 1

        val foundReview: Optional<Review?> = reviewRepository!!.findById(reviewId)
        assertTrue(foundReview.isPresent, "Review should be presented")

        val review = foundReview.get().copy(
            reviewName = content,
            reviewDetail = detail,
            rating = star
        )

        reviewRepository.save(review) // 변경된 review 객체를 저장합니다.

        val updatedReview = reviewRepository.findById(reviewId).get()
        assertEquals(content, updatedReview.reviewName)
        assertEquals(detail, updatedReview.reviewDetail)
        assertEquals(star, updatedReview.rating)
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.Order(4)
    fun testDeleteById() {
        val reviewID = 4L

        org.junit.jupiter.api.Assertions.assertTrue(
            reviewRepository!!.findById(reviewID).isPresent,
            "foundReview should be present"
        )

        reviewRepository.deleteById(reviewID)

        org.junit.jupiter.api.Assertions.assertFalse(
            reviewRepository.findById(reviewID).isPresent,
            "foundReview should not be present"
        )
    }
}