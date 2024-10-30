package edu.example.learner.courseabout.coursereview.service

import edu.example.learner.courseabout.coursereview.dto.ReviewDTO
import edu.example.learner.courseabout.coursereview.entity.Review
import edu.example.learner.courseabout.coursereview.entity.ReviewType
import edu.example.learner.courseabout.coursereview.repository.ReviewRepository
import edu.example.learner_kotlin.courseabout.coursereview.exception.ReviewException
import edu.example.learner_kotlin.log
import jakarta.transaction.Transactional
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service


@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val courseService: CourseServiceImpl,
    private val memberService: MemberService,
    private val modelMapper: ModelMapper,
) : ReviewService {

    @Transactional
    override fun createReview(reviewDTO: ReviewDTO, reviewType: ReviewType?): ReviewDTO {
        try {
            val course = courseService!!.readReview(reviewDTO.courseId).toEntity()

            log.info("Creating review for course $course")
            val memberInfo = memberService!!.getMemberInfo(reviewDTO.writerId)
            log.info("$course.member.nickname")

            if (reviewDTO.writerId == null) {
                throw ReviewException.NOT_LOGIN.get()
            } else if (memberInfo.nickname === course.member.nickname) {
                throw ReviewException.NOT_MATCHED_REVIEWER.get()
            }

            reviewDTO.apply {
                this.reviewType = reviewType
            }

            val review = modelMapper.map(reviewDTO, Review::class.java)
            log.info(review)

            reviewRepository!!.save(review)
            return ReviewDTO(review)
        } catch (e: Exception) {
            log.error("--- e : $e")
            error("--- " + e.message) //에러 로그로 발생 예외의 메시지를 기록하고
            throw ReviewException.NOT_REGISTERED.get()
        }
    }

    override fun getReviewById(reviewId: Long): ReviewDTO {
        val review = reviewRepository!!.findById(reviewId).orElseThrow { ReviewException.NOT_FOUND.get() }!!
        return ReviewDTO(review)
    }

    override fun updateReview(reviewId: Long, reviewDTO: ReviewDTO): ReviewDTO {
        val course = courseService!!.read(reviewDTO.courseId)!!.toEntity()
        log.info(course)

        val review = reviewRepository!!.findById(reviewId).orElseThrow { ReviewException.NOT_FOUND.get() }!!
        if (review.member?.memberId !== reviewDTO.writerId) {
            throw ReviewException.NOT_MATCHED_REVIEWER.get()
        }

        if (reviewDTO.rating == 0) {
            reviewDTO.rating = 1
        }
        reviewDTO.reviewDetail = review.reviewDetail

        val newReview = modelMapper.map(reviewDTO, Review::class.java)

        try {
            review.apply {
                reviewName=newReview.reviewName
                reviewDetail = newReview.reviewDetail
                rating = newReview.rating
            }

            reviewRepository.save(review)
            return ReviewDTO(review)
        } catch (e: Exception) {
            log.error(e.message)
            throw ReviewException.NOT_MODIFIED.get()
        }
    }

    override fun deleteReview(reviewId: Long, reviewDTO: ReviewDTO) {
        log.info(reviewDTO)
        val review = reviewRepository!!.findById(reviewId).orElseThrow { ReviewException.NOT_FOUND.get() }!!

        if (review.member?.memberId !== reviewDTO.writerId) {
            throw ReviewException.NOT_MATCHED_REVIEWER.get()
        }

        try {
            reviewRepository.delete(review)
        } catch (e: Exception) {
            log.error(e.message)
            throw ReviewException.NOT_REMOVED.get()
        }
    }

    override fun getCourseReviewList(courseId: Long?): List<ReviewDTO> {
        // COURSE인 리뷰만 가져오기 위해 필터링

        val reviewList = reviewRepository!!.getCourseReview(courseId).orElse(null)
        val reviewDTOList: MutableList<ReviewDTO> = ArrayList()

        if (reviewList == null || reviewList.isEmpty()) {
            return reviewDTOList
        }

        // 리뷰 타입이 COURSE인 것만 필터링
        reviewList.filter { it?.reviewType == ReviewType.COURSE }
            .forEach { review ->
                reviewDTOList.add(
                    ReviewDTO(
                        reviewId = review?.reviewId,
                        reviewName = review?.reviewName,
                        reviewDetail = review?.reviewDetail,
                        rating = review?.rating,
                        reviewType = review?.reviewType,
                        reviewUpdatedDate = review?.reviewUpdatedDate,
                        writerId = review?.member?.memberId,
                        courseId = courseId,
                        writerName = review?.member?.nickname,
                        nickname = review?.course?.member?.nickname
                    )
                )
            }

        return reviewDTOList

    }


    override fun getInstructorReviewList(courseId: Long?, nickname: String?): List<ReviewDTO> {
        val reviewList = reviewRepository!!.getInstructorReview(nickname).orElse(null)

        val reviewDTOList: MutableList<ReviewDTO> = ArrayList()
        if (reviewList == null || reviewList.isEmpty()) {
            return reviewDTOList
        }

        // 리뷰 타입이 INSTRUCTOR인 것만 필터링
        reviewList.filter { it?.reviewType == ReviewType.INSTRUCTOR }
            .forEach { review ->
                reviewDTOList.add(
                    ReviewDTO(
                        reviewId = review?.reviewId,
                        reviewName = review?.reviewName,
                        reviewDetail = review?.reviewDetail,
                        rating = review?.rating,
                        reviewType = review?.reviewType,
                        reviewUpdatedDate = review?.reviewUpdatedDate,
                        writerId = review?.member?.memberId,
                        courseName = review?.course?.courseName,
                        courseId = courseId,
                        nickname = nickname,
                        writerName = review?.member?.nickname
                    )
                )
            }

        return reviewDTOList
    }

    override fun readInstructorReview(nickname: String?, reviewId: Long): ReviewDTO? {
        val reviewList = reviewRepository!!.getInstructorReview(nickname).orElse(null)

        if (reviewList == null || reviewList.isEmpty()) {
            return ReviewDTO()
        }

        return reviewList
            .firstOrNull { it?.reviewId == reviewId } // 첫 번째 일치하는 리뷰 찾기
            ?.let { review ->
                ReviewDTO(
                    reviewId = review.reviewId,
                    reviewName = review.reviewName,
                    reviewDetail = review.reviewDetail,
                    rating = review.rating,
                    reviewType = review.reviewType,
                    reviewUpdatedDate = review.reviewUpdatedDate,
                    writerId = review.member?.memberId,
                    courseName = review.course.courseName,
                    courseId = review.course.courseId,
                    nickname = nickname,
                    writerName = review.member?.nickname
                )
            }
    }


    override fun readCourseReview(courseId: Long?, reviewId: Long): ReviewDTO? {
        val reviewList = reviewRepository.getCourseReview(courseId).orElse(null)

        if (reviewList == null || reviewList.isEmpty()) {
            return ReviewDTO()
        }

        val reviewDTO = reviewList
            .firstOrNull { it?.reviewType == ReviewType.COURSE && it.reviewId == reviewId } // 첫 번째 일치하는 리뷰 찾기
            ?.let { review ->
                ReviewDTO(
                    reviewId = review.reviewId,
                    reviewName = review.reviewName,
                    reviewDetail = review.reviewDetail,
                    rating = review.rating,
                    reviewType = review.reviewType,
                    reviewUpdatedDate = review.reviewUpdatedDate,
                    writerId = review.member?.memberId,
                    courseName = review.course.courseName,
                    courseId = courseId,
                    nickname = review.course.member.nickname,
                    writerName = review.member?.nickname
                )
            }

        log.info(reviewDTO.toString())
        return reviewDTO
    }
}