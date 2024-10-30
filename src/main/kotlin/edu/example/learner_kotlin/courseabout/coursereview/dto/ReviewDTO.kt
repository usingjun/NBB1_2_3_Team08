package edu.example.learner.courseabout.coursereview.dto

import edu.example.learner.courseabout.coursereview.entity.Review
import edu.example.learner.courseabout.coursereview.entity.ReviewType
import java.time.LocalDateTime

data class ReviewDTO(
    var reviewId: Long? = null,
    var reviewName: String? = null,
    var reviewDetail: String? = null,
    var rating: Int? = null,
    var reviewUpdatedDate: LocalDateTime? = null,
    var reviewType: ReviewType? = null,
    var writerId: Long? = null,
    var writerName: String? = null,
    var courseName: String? = null,
    var nickname: String? = null,
    var courseId: Long? = null
) {
    constructor(review: Review) : this(
        review.reviewId,
        review.reviewName,
        review.reviewDetail,
        review.rating,
        review.reviewUpdatedDate,
        review.reviewType,
        review.member?.memberId,
        review.member?.nickname,
        review.course?.courseName,
        review.course?.member?.nickname,
        review.course?.courseId
    )
}
