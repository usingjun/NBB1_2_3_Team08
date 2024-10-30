package edu.example.learner_kotlin.courseabout.service

import edu.example.learner.courseabout.coursereview.dto.ReviewDTO
import edu.example.learner.courseabout.coursereview.entity.ReviewType
import edu.example.learner.courseabout.coursereview.repository.ReviewRepository
import edu.example.learner.courseabout.coursereview.service.ReviewService
import edu.example.learner_kotlin.log
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.IntStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ReviewServiceTest(
    private val reviewService: ReviewService? = null,
    private val reviewRepository: ReviewRepository? = null
) {

    @Test
    @Order(1)
    fun testRegister() {
        IntStream.rangeClosed(1, 5).forEach { i: Int ->
            //GIVEN
            val reviewDTO: ReviewDTO =
                ReviewDTO().apply {
                    this.reviewName="title$i"
                    this.reviewDetail="content$i"
                    this.courseId=1L
                    this.writerId=1L
                    this.rating=5
                    this.reviewType=ReviewType.COURSE
                }
            val reviewDTO2: ReviewDTO =
                ReviewDTO().apply {
                    this.reviewName="title$i"
                    this.reviewDetail="content$i"
                    this.courseId=1L
                    this.writerId=1L
                    this.rating=5
                    this.reviewType=ReviewType.INSTRUCTOR
                }

            //WHEN
            val registeredReviewDTO1 =
                reviewService!!.createReview(reviewDTO, reviewDTO.reviewType)
            val registeredReviewDTO2 =
                reviewService.createReview(reviewDTO2, reviewDTO2.reviewType)

            //THEN
            assertNotNull(registeredReviewDTO1)
            assertEquals(2L * i - 1, registeredReviewDTO1.reviewId)

            log.info("--- registeredReviewDTO: $registeredReviewDTO1")
            log.info("--- registeredReviewDTO2: $registeredReviewDTO2")
        }
    }

    @Test
    @Order(2)
    fun testRead() {
        //GIVEN
        val reviewId = 1L

        //WHEN
        val reviewDTO = reviewService!!.getReviewById(reviewId)

        //THEN
        assertNotNull(reviewDTO)
        assertEquals(reviewId, reviewDTO.reviewId)

        log.info("--- foundFAQDTO: $reviewDTO")
    }

    @Test
    @Order(3)
    fun testReadAll() {
        //GIVEN
        val reviewDTO = reviewService!!.getReviewById(1L)

        //WHEN
        val reviewDTOList = reviewService.getCourseReviewList(1L)

        //THEN
        assertNotNull(reviewDTOList)
        assertEquals(10, reviewDTOList.size)

        log.info("--- faqDTOList: $reviewDTOList")
    }


    @Test
    @Order(4)
    fun testUpdate() {
        //GIVEN
        val reviewDTO: ReviewDTO =
            ReviewDTO().apply {
                this.reviewId=10L
                this.reviewName="new title"
                this.reviewDetail="new content"
                this.reviewType=ReviewType.COURSE
            }


        //WHEN
        val updatedReviewDTO = reviewService!!.updateReview(10L, reviewDTO)

        //THEN
        assertNotNull(updatedReviewDTO)
        assertEquals("new title", updatedReviewDTO.reviewName)
        assertEquals("new content", updatedReviewDTO.reviewDetail)
        assertEquals(ReviewType.COURSE, updatedReviewDTO.reviewType)

        log.info("--- updatedReviewDTO: $updatedReviewDTO")
    }

    @Test
    @Order(5)
    fun testDelete() {
        //GIVEN
        val reviewId = 9L
        val reviewDTO: ReviewDTO =
            ReviewDTO().apply {
                this.reviewId=reviewId
                this.reviewName="new title"
                this.reviewDetail="new content"
                this.reviewType=ReviewType.COURSE
            }

        //WHEN
        reviewService!!.deleteReview(reviewId, reviewDTO)

        //THEN
        assertFalse(reviewRepository!!.existsById(reviewId))
    }
}
