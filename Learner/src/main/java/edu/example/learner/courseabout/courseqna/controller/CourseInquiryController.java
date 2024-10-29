package edu.example.learner.courseabout.courseqna.controller;

import edu.example.learner.courseabout.courseqna.dto.CourseInquiryDTO;
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry;
import edu.example.learner.courseabout.courseqna.service.CourseInquiryService;
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
@RequestMapping("/course/{courseId}/course-inquiry")
@Tag(name = "강의 문의 컨트롤러", description = "강의 문의 CURD에 대한 API")
public class CourseInquiryController {
    private final CourseInquiryService courseInquiryService;

    //강의 문의 등록
    @PostMapping
    @Operation(summary = "강의 문의 등록", description = "강의 문의 제목 및 내용을 등록합니다.")
    public ResponseEntity<CourseInquiryDTO> register(@RequestBody CourseInquiryDTO courseInquiryDTO){
        return ResponseEntity.ok(courseInquiryService.register(courseInquiryDTO));
    }

    //강의 문의 리스트 조회
    @GetMapping
    @Operation(summary = "강의 문의 리스트 조회", description = "해당 강의에 대한 문의 리스트를 조회합니다.")
    public ResponseEntity<List<CourseInquiryDTO>> getList(@PathVariable("courseId") Long courseId){
        List<CourseInquiry> courseInquiries = courseInquiryService.readAll(courseId);
        List<CourseInquiryDTO> courseInquiryDTOS = courseInquiries
                .stream()
                .map(CourseInquiryDTO::new)
                .toList();
        log.info(courseInquiryDTOS);
        return ResponseEntity.ok(courseInquiryDTOS);
    }

    //강의 문의 조회
    @GetMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 조회", description = "강의 문의에 대한 내용을 조회합니다.")
    public ResponseEntity<CourseInquiryDTO> getInquiry( @PathVariable("inquiryId") Long inquiryId ){
        return ResponseEntity.ok(courseInquiryService.read(inquiryId));
    }

    //강의 문의 상태 수정
    @PutMapping("/{inquiryId}/status")
    @Operation(summary = "강의 문의 상태 수정", description = "강의 문의에 대한 상태를 수정합니다.")
    public ResponseEntity<CourseInquiryDTO> modifyStatus ( @PathVariable("inquiryId") Long inquiryId ,
                                                           @RequestBody CourseInquiryDTO courseInquiryDTO){
        courseInquiryDTO.setInquiryId(inquiryId);
        return ResponseEntity.ok(courseInquiryService.updateStatus(courseInquiryDTO));
    }

    //강의 문의 삭제
    @DeleteMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 삭제", description = "강의 문의를 삭제합니다.")
    public ResponseEntity<Void> remove( @PathVariable("inquiryId") Long inquiryId ){
        courseInquiryService.delete(inquiryId);
        return ResponseEntity.ok().build();
    }
}

