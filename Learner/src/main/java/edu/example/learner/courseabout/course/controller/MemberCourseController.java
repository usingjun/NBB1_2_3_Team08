package edu.example.learner.courseabout.course.controller;

import edu.example.learner.courseabout.course.service.MemberCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
@Tag(name = "MemberCourse", description = "회원 강의 구매 API")
public class MemberCourseController {

    private final MemberCourseService memberCourseService;

    // 구매 여부 확인
    @GetMapping("/{courseId}/purchase")
    @Operation(summary = "강의 구매 확인", description = "회원이 해당 강의를 구매했는지 확인합니다.")
    public ResponseEntity<Boolean> checkPurchase(@PathVariable Long courseId, @RequestParam Long memberId) {
        boolean isPurcheased = memberCourseService.checkPurchase(courseId, memberId);

        return ResponseEntity.ok(isPurcheased);
    }

}
