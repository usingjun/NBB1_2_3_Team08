package edu.example.learner.qna.faq.controller;

import edu.example.learner.qna.faq.dto.FAQDTO;
import edu.example.learner.qna.faq.service.FAQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/faqs")
@RequiredArgsConstructor
@Tag(name = "FAQ 컨트롤러", description = "FAQ 등록, 조회, 수정, 삭제 관련 API")
public class FAQRestController {
    private final FAQService faqService;

    @GetMapping("/{faqId}")
    @Operation(summary = "FAQ 조회", description = "FAQ 아이디를 기반으로 FAQ를 조회합니다.")
    public ResponseEntity<FAQDTO> read(@PathVariable("faqId") Long faqId) {
        return ResponseEntity.ok(faqService.read(faqId));
    }

    @GetMapping
    @Operation(summary = "전체 FAQ 조회", description = "전체 FAQ를 목록으로 조회합니다.")
    public ResponseEntity<List<FAQDTO>> readAll() {
        return ResponseEntity.ok(faqService.readAll());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "카테고리별 FAQ 조회", description = "FAQ를 카테고리 별로 조회합니다.")
    public ResponseEntity<List<FAQDTO>> readByCategory(@PathVariable("category") String category) {
        String categoryUpperCase = category.toUpperCase();
        return ResponseEntity.ok(faqService.readByCategory(categoryUpperCase));
    }

    @PostMapping
    @Operation(summary = "FAQ 등록", description = "FAQ를 등록합니다.")
    public ResponseEntity<FAQDTO> create(@Validated @RequestBody FAQDTO faqDTO) {
        return ResponseEntity.ok(faqService.register(faqDTO));
    }

    @PutMapping("/{faqId}")
    @Operation(summary = "FAQ 수정", description = "FAQ를 수정합니다.")
    public ResponseEntity<FAQDTO> update(@PathVariable("faqId") Long faqId, @Validated @RequestBody FAQDTO faqDTO) {
        faqDTO.setFaqId(faqId);
        return ResponseEntity.ok(faqService.update(faqDTO));
    }

    @DeleteMapping("/{faqId}")
    @Operation(summary = "FAQ 삭제", description = "FAQ를 삭제합니다.")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("faqId") Long faqId) {
        faqService.delete(faqId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
