package edu.example.learner_kotlin.courseabout.courseqna.courseanswer.controller

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.dto.CourseAnswerDTO
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.service.CourseAnswerService
import edu.example.learner_kotlin.log
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course/{courseId}/course-answer")
@Tag(name = "강의 문의 답변 컨트롤러", description = "강의 문의 답변 CURD에 대한 API")
class CourseAnswerController(
    private val courseAnswerService: CourseAnswerService,
    private val modelMapper: ModelMapper
) {

    // 강의 문의 답변 생성
    @PostMapping
    @Operation(summary = "강의 문의 답변 등록", description = "강의 문의에 대한 답변을 등록합니다.")
    fun register(@RequestBody courseAnswerDTO: CourseAnswerDTO): ResponseEntity<CourseAnswerDTO> {
        val courseAnswer = courseAnswerService.register(courseAnswerDTO)
        val responseDTO = modelMapper.map(courseAnswer, CourseAnswerDTO::class.java)
        return ResponseEntity.ok(responseDTO)
    }

    // 강의 문의 답변 전체 보기
    @GetMapping("/{inquiryId}")
    @Operation(summary = "강의 문의 답변 리스트 조회", description = "해당 강의 문의에 대한 답변 리스트를 조회합니다.")
    fun getList(@PathVariable("inquiryId") inquiryId: Long): ResponseEntity<List<CourseAnswerDTO>> {
        val courseAnswers = courseAnswerService.readAll(inquiryId)
        val courseAnswerDTOList = courseAnswers.map { modelMapper.map(it, CourseAnswerDTO::class.java) }

        // 프로필 이미지를 제외하고 다른 필드만 로그로 출력
        courseAnswerDTOList.forEach { answer ->
            log.info(
                "CourseAnswer: inquiryId={}, answerId={}, answerContent={}, memberNickname={}",
                answer.inquiryId, answer.answerId, answer.answerContent, answer.memberNickname
            )
        }
        return ResponseEntity.ok(courseAnswerDTOList)
    }

    // 강의 문의 답변 수정
    @PutMapping("/{answerId}")
    @Operation(summary = "강의 문의 답변 수정", description = "강의 문의 답변을 수정합니다.")
    fun modify(
        @PathVariable("answerId") answerId: Long,
        @RequestBody courseAnswerDTO: CourseAnswerDTO
    ): ResponseEntity<CourseAnswerDTO> {
        courseAnswerDTO.answerId = answerId
        val updatedCourseAnswer = courseAnswerService.update(courseAnswerDTO)
        val responseDTO = modelMapper.map(updatedCourseAnswer, CourseAnswerDTO::class.java)
        return ResponseEntity.ok(responseDTO)
    }

    // 강의 문의 답변 삭제
    @DeleteMapping("/{answerId}")
    @Operation(summary = "강의 문의 답변 삭제", description = "강의 문의 답변을 삭제합니다.")
    fun remove(@PathVariable("answerId") answerId: Long): ResponseEntity<Void> {
        courseAnswerService.delete(answerId)
        return ResponseEntity.ok().build()
    }
}