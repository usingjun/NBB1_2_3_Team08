package edu.example.learner_kotlin.courseabout.course.controller

import edu.example.learner_kotlin.courseabout.course.service.MemberCourseService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course")
@Tag(name = "MemberCourse", description = "회원 강의 구매 API")
class MemberCourseController (
    private val memberCourseService: MemberCourseService
){


    // 구매 여부 확인
    @GetMapping("/{courseId}/purchase")
    @Operation(summary = "강의 구매 확인", description = "회원이 해당 강의를 구매했는지 확인합니다.")
    fun checkPurchase(@PathVariable courseId: Long?, @RequestParam memberId: Long?): ResponseEntity<Boolean> {
        val isPurcheased: Boolean = memberCourseService.checkPurchase(courseId, memberId)

        return ResponseEntity.ok(isPurcheased)
    }
}