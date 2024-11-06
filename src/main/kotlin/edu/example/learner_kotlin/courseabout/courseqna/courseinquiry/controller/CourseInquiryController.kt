package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.controller

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.dto.CourseInquiryDTO
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.service.CourseInquiryService
import edu.example.learner_kotlin.log
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course/{courseId}/course-inquiry")
@Tag(name = "강의 문의 컨트롤러", description = "강의 문의 CURD에 대한 API")
class CourseInquiryController(
    private val courseInquiryService: CourseInquiryService,
    private val modelMapper: ModelMapper
) {

    // 강의 문의 등록
    @PostMapping
    @Operation(summary = "강의 문의 등록", description = "강의 문의 제목 및 내용을 등록합니다.")
    fun register(@PathVariable courseId: Long, @RequestBody courseInquiryDTO: CourseInquiryDTO): ResponseEntity<CourseInquiryDTO> {
        courseInquiryDTO.courseId = courseId
        return ResponseEntity.ok(courseInquiryService.register(courseInquiryDTO))
    }

    // 강의 문의 리스트 조회
    @GetMapping
    @Operation(summary = "강의 문의 리스트 조회", description = "해당 강의에 대한 문의 리스트를 조회합니다.")
    fun getList(@PathVariable("courseId") courseId: Long): ResponseEntity<List<CourseInquiryDTO>> {
        val courseInquiries = courseInquiryService.readAll(courseId)
        val courseInquiryDTOS = courseInquiries?.map { modelMapper.map(it, CourseInquiryDTO::class.java) } ?: emptyList()

        // 필수 필드만 로깅
        courseInquiryDTOS.forEach { inquiry ->
            log.info("CourseInquiry: inquiryId={}, inquiryTitle={}, memberNickname={}",
                inquiry.inquiryId, inquiry.inquiryTitle, inquiry.memberNickname)
        }

        return ResponseEntity.ok(courseInquiryDTOS)
    }

    // 강의 문의 리스트 조회 - 상태 순으로 정렬
    @GetMapping("/sorted")
    @Operation(summary = "상태 순으로 정렬된 강의 문의 리스트 조회", description = "해당 강의에 대한 문의를 상태로 정렬하여 리스트 조회를 합니다.")
    fun getSortedList(@PathVariable("courseId") courseId: Long): ResponseEntity<List<CourseInquiryDTO>> {
        val courseInquiries = courseInquiryService.readSortedInquiry(courseId)
        val courseInquiryDTOS = courseInquiries?.map { modelMapper.map(it, CourseInquiryDTO::class.java) } ?: emptyList()

        // 필수 필드만 로깅
        courseInquiryDTOS.forEach { inquiry ->
            log.info("CourseInquiry: inquiryId={}, inquiryTitle={}, memberNickname={}",
                inquiry.inquiryId, inquiry.inquiryTitle, inquiry.memberNickname)
        }

        return ResponseEntity.ok(courseInquiryDTOS)
    }

    // 강의 문의 조회
    @GetMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 조회", description = "강의 문의에 대한 내용을 조회합니다.")
    fun getInquiry(@PathVariable("inquiryId") inquiryId: Long): ResponseEntity<CourseInquiryDTO> {
        val inquiry = courseInquiryService.read(inquiryId)

        log.info("CourseInquiryDTO: inquiryId={}, inquiryTitle={}, memberNickname={}",
            inquiry.inquiryId, inquiry.inquiryTitle, inquiry.memberNickname)

        return ResponseEntity.ok(inquiry)
    }

    // 강의 문의 상태 수정
    @PutMapping("/{inquiryId}/status")
    @Operation(summary = "강의 문의 상태 수정", description = "강의 문의에 대한 상태를 수정합니다.")
    fun modifyStatus(@PathVariable("inquiryId") inquiryId: Long,
                     @RequestBody courseInquiryDTO: CourseInquiryDTO
    ): ResponseEntity<CourseInquiryDTO> {
        courseInquiryDTO.inquiryId = inquiryId
        requireNotNull(courseInquiryDTO.inquiryStatus) { "Status must be provided" }
        return ResponseEntity.ok(courseInquiryService.updateStatus(courseInquiryDTO))
    }

    // 강의 문의 삭제
    @DeleteMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 삭제", description = "강의 문의를 삭제합니다.")
    fun remove(@PathVariable("inquiryId") inquiryId: Long): ResponseEntity<Void> {
        courseInquiryService.delete(inquiryId)
        return ResponseEntity.ok().build()
    }
}
