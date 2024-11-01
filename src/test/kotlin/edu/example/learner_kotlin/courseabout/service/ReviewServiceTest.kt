//package edu.example.learner_kotlin.courseabout.service
//
//import lombok.extern.log4j.Log4j2
//
//@SpringBootTest
//@Log4j2
//@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
//class ReviewServiceTest {
//    @Autowired
//    private val reviewService: ReviewService? = null
//
//    @Autowired
//    private val reviewRepository: ReviewRepository? = null
//
//    @org.junit.jupiter.api.Test
//    @org.junit.jupiter.api.Order(1)
//    fun testRegister() {
//        java.util.stream.IntStream.rangeClosed(1, 5).forEach { i: Int ->
//            //GIVEN
//            val reviewDTO: ReviewDTO = ReviewDTO.builder()
//                .reviewName("title$i")
//                .reviewDetail("content$i")
//                .courseId(1L)
//                .writerId(1L)
//                .rating(5)
//                .reviewType(ReviewType.COURSE)
//                .build()
//            val reviewDTO2: ReviewDTO = ReviewDTO.builder()
//                .reviewName("title $i")
//                .reviewDetail("content$i")
//                .courseId(1L)
//                .writerId(1L)
//                .rating(5)
//                .reviewType(ReviewType.INSTRUCTOR)
//                .build()
//
//            //WHEN
//            val registeredReviewDTO1: ReviewDTO = reviewService.createReview(reviewDTO, reviewDTO.reviewType)
//            val registeredReviewDTO2: ReviewDTO = reviewService.createReview(reviewDTO2, reviewDTO2.reviewType)
//
//            //THEN
//            org.junit.jupiter.api.Assertions.assertNotNull(registeredReviewDTO1)
//            org.junit.jupiter.api.Assertions.assertEquals(2L * i - 1, registeredReviewDTO1.reviewId)
//
//            log.info("--- registeredReviewDTO: $registeredReviewDTO1")
//            log.info("--- registeredReviewDTO2: $registeredReviewDTO2")
//        }
//    }
//
//    @org.junit.jupiter.api.Test
//    @org.junit.jupiter.api.Order(2)
//    fun testRead() {
//        //GIVEN
//        val reviewId = 1L
//
//        //WHEN
//        val reviewDTO: ReviewDTO = reviewService.getReviewById(reviewId)
//
//        //THEN
//        org.junit.jupiter.api.Assertions.assertNotNull(reviewDTO)
//        org.junit.jupiter.api.Assertions.assertEquals(reviewId, reviewDTO.reviewId)
//
//        log.info("--- foundFAQDTO: $reviewDTO")
//    }
//
//    @org.junit.jupiter.api.Test
//    @org.junit.jupiter.api.Order(3)
//    fun testReadAll() {
//        //GIVEN
//        val reviewDTO: ReviewDTO = reviewService.getReviewById(1L)
//
//        //WHEN
//        val reviewDTOList: List<ReviewDTO> = reviewService.getCourseReviewList(1L)
//
//        //THEN
//        org.junit.jupiter.api.Assertions.assertNotNull(reviewDTOList)
//        org.junit.jupiter.api.Assertions.assertEquals(10, reviewDTOList.size)
//
//        log.info("--- faqDTOList: $reviewDTOList")
//    }
//
//
//    @org.junit.jupiter.api.Test
//    @org.junit.jupiter.api.Order(4)
//    fun testUpdate() {
//        //GIVEN
//        val reviewDTO: ReviewDTO = ReviewDTO.builder()
//            .reviewId(10L)
//            .reviewName("new title")
//            .reviewDetail("new content")
//            .reviewType(ReviewType.COURSE)
//            .build()
//
//        //WHEN
//        val updatedReviewDTO: ReviewDTO = reviewService.updateReview(10L, reviewDTO)
//
//        //THEN
//        org.junit.jupiter.api.Assertions.assertNotNull(updatedReviewDTO)
//        org.junit.jupiter.api.Assertions.assertEquals("new title", updatedReviewDTO.reviewName)
//        org.junit.jupiter.api.Assertions.assertEquals("new content", updatedReviewDTO.reviewDetail)
//        org.junit.jupiter.api.Assertions.assertEquals(ReviewType.COURSE, updatedReviewDTO.reviewType)
//
//        log.info("--- updatedReviewDTO: $updatedReviewDTO")
//    }
//
//    @org.junit.jupiter.api.Test
//    @org.junit.jupiter.api.Order(5)
//    fun testDelete() {
//        //GIVEN
//        val reviewId = 9L
//        val reviewDTO: ReviewDTO = ReviewDTO.builder()
//            .reviewId(reviewId)
//            .reviewName("new title")
//            .reviewDetail("new content")
//            .reviewType(ReviewType.COURSE)
//            .build()
//
//        //WHEN
//        reviewService.deleteReview(reviewId, reviewDTO)
//
//        //THEN
//        org.junit.jupiter.api.Assertions.assertFalse(reviewRepository.existsById(reviewId))
//    }
//}
