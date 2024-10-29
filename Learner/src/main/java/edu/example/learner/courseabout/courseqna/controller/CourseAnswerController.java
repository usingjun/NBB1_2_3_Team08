package edu.example.learner.courseabout.courseqna.controller;

import edu.example.learner.courseabout.courseqna.dto.CourseAnswerDTO;
import edu.example.learner.courseabout.courseqna.entity.CourseAnswer;
import edu.example.learner.courseabout.courseqna.service.CourseAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/course/{courseId}/course-answer")
@Tag(name = "강의 문의 답변 컨트롤러", description = "강의 문의 답변 CURD에 대한 API")
public class CourseAnswerController {
    private final CourseAnswerService courseAnswerService;

    //강의 문의 답변 생성
    @PostMapping
    @Operation(summary = "강의 문의 답변 등록", description = "강의 문의에 대한 답변을 등록합니다.")
    public ResponseEntity<CourseAnswerDTO> register(@RequestBody CourseAnswerDTO courseAnswerDTO){
        return ResponseEntity.ok(courseAnswerService.register(courseAnswerDTO));
    }

    //강의 문의 답변 전체 보기
    @GetMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 답변 리스트 조회", description = "해당 강의 문의에 대한 답변 리스트를 조회합니다.")
    public ResponseEntity<List<CourseAnswerDTO>> getList( @PathVariable("inquiryId") Long inquiryId){
        return ResponseEntity.ok(courseAnswerService.readAll(inquiryId));
    }

    //강의 문의 답변 수정
    @PutMapping("/{answerId}")
    @Operation(summary = "강의 문의 답변 수정", description = "강의 문의 답변을 수정합니다.")
    public ResponseEntity<CourseAnswerDTO> modify( @PathVariable("answerId") Long answerId,
                                                    @RequestBody CourseAnswerDTO courseAnswerDTO){

        courseAnswerDTO.setAnswerId(answerId);
        return ResponseEntity.ok(courseAnswerService.update(courseAnswerDTO));
    }

    //강의 문의 답변 삭제
    @DeleteMapping("/{answerId}")
    @Operation(summary = "강의 문의 답변 삭제", description = "강의 문의 답변을 삭제합니다.")
    public ResponseEntity<Void> remove( @PathVariable("answerId") Long answerId){
        courseAnswerService.delete(answerId);
        return ResponseEntity.ok().build();
    }
}

