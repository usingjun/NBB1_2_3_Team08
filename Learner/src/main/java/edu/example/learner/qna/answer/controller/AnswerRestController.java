package edu.example.learner.qna.answer.controller;

import edu.example.learner.qna.answer.dto.AnswerDTO;
import edu.example.learner.qna.answer.service.AnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "문의 답변 컨트롤러", description = "문의 답변의 등록, 조회, 수정, 삭제 관련 API")
public class AnswerRestController {
    private final AnswerService answerService;

    @GetMapping("/{inquiryId}")
    @Operation(summary = "문의 답변 조회", description = "문의 아이디를 기반으로 문의 답변을 조회합니다.")
    public ResponseEntity<AnswerDTO> read(@PathVariable("inquiryId") Long inquiryId) {
        return ResponseEntity.ok(answerService.read(inquiryId));
    }

    @GetMapping
    @Operation(summary = "전체 문의 답변 조회", description = "전체 문의 답변을 목록으로 조회합니다.")
    public ResponseEntity<List<AnswerDTO>> readAll() {
        return ResponseEntity.ok(answerService.readAll());
    }

    @PostMapping
    @Operation(summary = "문의 답변 등록", description = "문의 답변을 등록합니다.")
    public ResponseEntity<AnswerDTO> create(@Validated @RequestBody AnswerDTO answerDTO) {
        return ResponseEntity.ok(answerService.register(answerDTO));
    }

    @PutMapping("/{answerId}")
    @Operation(summary = "문의 답변 수정", description = "문의 답변을 수정합니다.")
    public ResponseEntity<AnswerDTO> update(@PathVariable("answerId") Long answerId, @Validated @RequestBody AnswerDTO answerDTO) {
        answerDTO.setAnswerId(answerId);
        return ResponseEntity.ok(answerService.update(answerDTO));
    }

    @DeleteMapping("/{inquiryId}")
    @Operation(summary = "문의 답변 삭제", description = "문의 답변을 삭제합니다.")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("inquiryId") Long inquiryId) {
        answerService.deleteByInquiryId(inquiryId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
