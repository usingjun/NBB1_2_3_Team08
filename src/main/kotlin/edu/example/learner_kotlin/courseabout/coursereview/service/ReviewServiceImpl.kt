package edu.example.learner_kotlin.courseabout.coursereview.service

import edu.example.learner_kotlin.alarm.service.AlarmService
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.course.entity.QCourse.course
import edu.example.learner_kotlin.courseabout.course.repository.CourseRepository
import edu.example.learner_kotlin.courseabout.course.service.CourseService
import edu.example.learner_kotlin.courseabout.coursereview.dto.ReviewDTO
import edu.example.learner_kotlin.courseabout.coursereview.entity.Review
import edu.example.learner_kotlin.courseabout.coursereview.entity.ReviewType
import edu.example.learner_kotlin.courseabout.coursereview.repository.ReviewRepository
import edu.example.learner_kotlin.courseabout.course.service.CourseServiceImpl
import edu.example.learner_kotlin.courseabout.coursereview.exception.ReviewException
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.repository.MemberRepository
import edu.example.learner_kotlin.member.service.MemberService
import jakarta.transaction.Transactional
import org.modelmapper.ModelMapper
import org.modelmapper.PropertyMap
import org.modelmapper.convention.MatchingStrategies
import org.springframework.stereotype.Service


@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val courseRepository: CourseRepository,
    private val memberRepository: MemberRepository,
    private val alarmService: AlarmService,
    private val modelMapper: ModelMapper,
) : ReviewService {

    @Transactional
    override fun createReview(reviewDTO: ReviewDTO, reviewType: ReviewType?): ReviewDTO {
        try {
            // courseId를 통해 영속 상태의 Course 엔티티를 조회
            val course = courseRepository.findById(reviewDTO.courseId!!)
                .orElseThrow { ReviewException.COURSE_NOT_FOUND.get() }

            log.info("Creating review for course $course")

            // writerId를 통해 영속 상태의 Member 엔티티를 조회
            val member = memberRepository.findById(reviewDTO.writerId!!)
                .orElseThrow { ReviewException.NOT_LOGIN.get() }

            log.info("Loaded Member: $member")
            log.info("Member nickname: ${member.nickname}")

            // Review 엔티티 생성
            val review = Review(
                reviewName = reviewDTO.reviewName,
                reviewDetail = reviewDTO.reviewDetail,
                rating = reviewDTO.rating ?: 0,
                member = member,  // 영속 상태의 member 사용
                course = course,
                reviewType = reviewType
            )

            log.info("Review to be saved: $review")

            val courseOwner = course.member // 강의 소유주
            val alarmContent = "${member.nickname}님이 귀하의 강의 '${course.courseName}'에 리뷰를 남겼습니다."
            val alarmTitle = "새 리뷰 알림"

            alarmService.createAlarm(courseOwner!!.memberId!!, alarmContent, alarmTitle)

            // 리뷰 저장
            reviewRepository.save(review)
            return ReviewDTO(review)
        } catch (e: Exception) {
            log.error("--- e : $e")
            throw ReviewException.NOT_REGISTERED.get()
        }
    }


    override fun getReviewById(reviewId: Long): ReviewDTO {
        val review = reviewRepository!!.findById(reviewId).orElseThrow { ReviewException.NOT_FOUND.get() }!!
        return ReviewDTO(review)
    }

    override fun updateReview(reviewId: Long, reviewDTO: ReviewDTO): ReviewDTO {
        val course = courseRepository.findById(reviewDTO.courseId!!)
            .orElseThrow { ReviewException.COURSE_NOT_FOUND.get() }

        log.info("Creating review for course $course")

        // writerId를 통해 영속 상태의 Member 엔티티를 조회
        val member = memberRepository.findById(reviewDTO.writerId!!)
            .orElseThrow { ReviewException.NOT_LOGIN.get() }

        log.info("Loaded Member: $member")

        // 영속 상태의 Review 엔티티 조회
        val review = reviewRepository.findById(reviewId)
            .orElseThrow { ReviewException.NOT_FOUND.get() }

        // 작성자 일치 여부 확인
        if (review?.member?.memberId != member.memberId) {
            throw ReviewException.NOT_MATCHED_REVIEWER.get()
        }

        // 기본 값 설정 및 업데이트 정보 반영
        if (reviewDTO.rating == 0) {
            reviewDTO.rating = 1
        }

        try {
            // 엔티티 필드에 직접 값 설정하여 업데이트
            review?.apply {
                reviewName = reviewDTO.reviewName
                reviewDetail = reviewDTO.reviewDetail
                rating = reviewDTO.rating ?: 1
                this.course = course  // 영속 상태의 Course 엔티티 사용
                this.member = member  // 영속 상태의 Member 엔티티 사용
            }

            // 수정된 리뷰 저장
            review?.let { reviewRepository.save(it) }
            return review?.let { ReviewDTO(it) }!!
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
                    courseName = review.course?.courseName,
                    courseId = review.course?.courseId,
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
                    courseName = review.course?.courseName,
                    courseId = courseId,
                    nickname = review.course?.member?.nickname,
                    writerName = review.member?.nickname
                )
            }

        log.info(reviewDTO.toString())
        return reviewDTO
    }
}