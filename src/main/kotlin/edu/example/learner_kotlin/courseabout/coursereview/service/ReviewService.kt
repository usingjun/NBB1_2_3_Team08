package edu.example.learner_kotlin.courseabout.coursereview.service

import edu.example.learner_kotlin.courseabout.coursereview.dto.ReviewDTO
import edu.example.learner_kotlin.courseabout.coursereview.entity.ReviewType
import org.springframework.stereotype.Service

@Service
interface ReviewService {
    fun createReview(reviewDTO: ReviewDTO, reviewType: ReviewType?): ReviewDTO
    fun getReviewById(reviewId: Long): ReviewDTO
    fun updateReview(reviewId: Long, reviewDTO: ReviewDTO): ReviewDTO
    fun deleteReview(reviewId: Long, reviewDTO: ReviewDTO)

    fun getCourseReviewList(courseId: Long?): List<ReviewDTO>
    fun getInstructorReviewList(courseId: Long?, nickname: String?): List<ReviewDTO>

    fun readInstructorReview(nickname: String?, reviewId: Long): ReviewDTO?
    fun readCourseReview(courseId: Long?, reviewId: Long): ReviewDTO?
}

