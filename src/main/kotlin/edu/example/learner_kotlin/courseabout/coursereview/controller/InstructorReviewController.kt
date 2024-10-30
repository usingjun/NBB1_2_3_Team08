package edu.example.learner_kotlin.courseabout.coursereview.controller

import edu.example.learner.courseabout.coursereview.dto.ReviewDTO
import edu.example.learner.courseabout.coursereview.entity.ReviewType
import edu.example.learner.courseabout.coursereview.service.ReviewServiceImpl
import edu.example.learner_kotlin.log
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members/instructor/{nickname}/reviews")
@Tag(name = "강사 리뷰 컨트롤러", description = "강사 리뷰 CRUD를 담당하는 컨트롤러입니다.")
class InstructorReviewController {
    private val reviewService: ReviewServiceImpl? = null

    @PostMapping("/create")
    @Operation(summary = "Instructor Review 생성", description = "강사 리뷰를 생성합니다.")
    fun create(
        @Parameter(description = "강사 리뷰 데이터") @RequestBody reviewDTO: ReviewDTO
    ): ResponseEntity<ReviewDTO> {
        log.info(reviewDTO.writerId)

        reviewDTO.courseId = reviewDTO.courseId
        log.info("Create review: $reviewDTO")

        return ResponseEntity.ok(reviewService!!.createReview(reviewDTO, ReviewType.INSTRUCTOR))
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Instructor Review 조회", description = "강사 리뷰를 조회합니다.")
    fun read(
        @Parameter(description = "강사 이름") @PathVariable nickname: String?,
        @Parameter(description = "강사 리뷰 ID") @PathVariable("reviewId") reviewId: Long
    ): ResponseEntity<ReviewDTO?> {
        return ResponseEntity.ok(reviewService!!.readInstructorReview(nickname, reviewId))
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Instructor Review 수정", description = "강사 리뷰를 수정합니다.")
    fun update(
        @Parameter(description = "강사 리뷰 ID") @PathVariable("reviewId") reviewId: Long,
        @Parameter(description = "강사 리뷰 데이터") @RequestBody reviewDTO: ReviewDTO
    ): ResponseEntity<ReviewDTO> {
        log.info("update Review: $reviewDTO")
        return ResponseEntity.ok(reviewService!!.updateReview(reviewId, reviewDTO))
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Instructor Review 삭제", description = "강사 리뷰를 삭제합니다.")
    fun remove(
        @Parameter(description = "강사 리뷰 ID") @PathVariable("reviewId") reviewId: Long,
        @Parameter(description = "강사 리뷰 데이터") @RequestBody reviewDTO: ReviewDTO
    ): ResponseEntity<Map<String, String>> {
        log.info("Delete Review: $reviewId")

        reviewService!!.deleteReview(reviewId, reviewDTO)
        return ResponseEntity.ok(java.util.Map.of("result", "success"))
    }

    @GetMapping("/list")
    @Operation(summary = "Instructor Review list 조회", description = "강사 리뷰 리스트를 조회합니다.")
    fun reviewList(
        @Parameter(description = "강사 이름") @PathVariable("nickname") nickname: String?,
        @Parameter(description = "강사 리뷰 데이터") reviewDTO: ReviewDTO
    ): ResponseEntity<List<ReviewDTO?>> {
        val courseId = reviewDTO.courseId
        return ResponseEntity.ok(reviewService!!.getInstructorReviewList(courseId, nickname))
    }
}
