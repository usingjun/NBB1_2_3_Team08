package edu.example.learner.qna.inquiry.controller;

import edu.example.learner.qna.inquiry.dto.InquiryDTO;
import edu.example.learner.qna.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inquiries")
@RequiredArgsConstructor
@Tag(name = "문의 컨트롤러", description = "문의 등록, 조회, 수정, 삭제 관련 API")
public class InquiryRestController {
    private final InquiryService inquiryService;

    @GetMapping("/{inquiryId}")
    @Operation(summary = "문의 조회", description = "문의 아이디를 기반으로 문의를 조회합니다.")
    public ResponseEntity<InquiryDTO> read(@PathVariable("inquiryId") Long inquiryId) {
        return ResponseEntity.ok(inquiryService.read(inquiryId));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "본인 문의 조회", description = "사용자 본인이 작성한 문의를 조회합니다.")
    public ResponseEntity<List<InquiryDTO>> readByMember(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(inquiryService.readByMemberId(memberId));
    }

    @GetMapping
    @Operation(summary = "전체 문의 조회", description = "전체 문의를 목록으로 조회합니다.")
    public ResponseEntity<List<InquiryDTO>> readAll() {
        return ResponseEntity.ok(inquiryService.readAll());
    }

    @PostMapping
    @Operation(summary = "문의 등록", description = "문의를 등록합니다.")
    public ResponseEntity<InquiryDTO> create(@Validated @RequestBody InquiryDTO inquiryDTO) {
        return ResponseEntity.ok(inquiryService.register(inquiryDTO));
    }

    @PutMapping("/{inquiryId}")
    @Operation(summary = "문의 수정", description = "문의를 수정합니다.")
    public ResponseEntity<InquiryDTO> update(@PathVariable("inquiryId") Long inquiryId, @Validated @RequestBody InquiryDTO inquiryDTO) {
        inquiryDTO.setInquiryId(inquiryId);
        return ResponseEntity.ok(inquiryService.update(inquiryDTO));
    }

    @PutMapping("/{inquiryId}/status")
    @Operation(summary = "문의 상태 수정", description = "문의 상태를 수정합니다.")
    public ResponseEntity<InquiryDTO> updateStatus(@PathVariable("inquiryId") Long inquiryId, @RequestBody String inquiryStatus) {
        InquiryDTO foundInquiryDTO = inquiryService.read(inquiryId);
        foundInquiryDTO.setInquiryStatus(inquiryStatus);
        return ResponseEntity.ok(inquiryService.update(foundInquiryDTO));
    }

    @DeleteMapping("/{inquiryId}")
    @Operation(summary = "문의 삭제", description = "문의를 삭제합니다.")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("inquiryId") Long inquiryId) {
        inquiryService.delete(inquiryId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
