package edu.example.learner.studytable.controller;

import edu.example.learner.studytable.dto.StudyTableDTO;
import edu.example.learner.studytable.service.StudyTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/study-tables")
@RequiredArgsConstructor
@Tag(name = "학습테이블 컨트롤러", description = "학습테이블 등록, 조회, 수정, 삭제 관련 API")
public class StudyTableController {
    private final StudyTableService studyTableService;

    @GetMapping("/{memberId}/weekly-summary")
    @Operation(summary = "주간 학습테이블 조회", description = "특정 주차의 학습테이블을 조회합니다.")
    public ResponseEntity<?> readWeeklySummaryByDate(@PathVariable("memberId") Long memberId, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Object> response = new HashMap<>();
        response.put("weeklyStudyTime", studyTableService.getWeeklyStudyTime(memberId, date));
        response.put("weeklyCompleted", studyTableService.getWeeklyCompleted(memberId, date));
        response.put("weeklySummary", studyTableService.getWeeklySummary(memberId, date));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/yearly-summary")
    @Operation(summary = "연간 학습테이블 조회", description = "특정 연도의 학습테이블을 조회합니다.")
    public ResponseEntity<?> readYearlySummary(@PathVariable("memberId") Long memberId, @RequestParam("year") int year) {
        Map<String, Object> response = new HashMap<>();
        response.put("yearlyCompleted", studyTableService.getYearlyCompleted(memberId, year));
        response.put("yearlyStudyTime", studyTableService.getYearlyStudyTime(memberId, year));
        response.put("yearlySummary", studyTableService.getYearlySummary(memberId, year));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "학습테이블 생성", description = "현재 날짜를 기반으로 학습테이블을 생성합니다.")
    public ResponseEntity<StudyTableDTO> create(@Validated @RequestBody StudyTableDTO studyTableDTO) {
        if (studyTableService.readByDate(studyTableDTO.getMemberId())) {
            return ResponseEntity.ok(studyTableDTO);
        }
        return ResponseEntity.ok(studyTableService.register(studyTableDTO));
    }

    @PutMapping("/{studyTableId}")
    @Operation(summary = "학습테이블 수정", description = "학습테이블 아이디를 기반으로 학습테이블을 수정합니다.")
    public ResponseEntity<StudyTableDTO> update(@Validated @RequestBody StudyTableDTO studyTableDTO, @PathVariable("studyTableId") Long studyTableId) {
        studyTableDTO.setStudyTableId(studyTableId);
        return ResponseEntity.ok(studyTableService.update(studyTableDTO));
    }

    @PutMapping("/today")
    @Operation(summary = "학습테이블 수정", description = "현재 날짜의 학습테이블을 조회하여 수정합니다.")
    public ResponseEntity<StudyTableDTO> updateWithDate(@RequestBody StudyTableDTO studyTableDTO) {
        studyTableDTO.setStudyTableId(studyTableService.readDTOByDate(studyTableDTO.getMemberId()).getStudyTableId());
        return ResponseEntity.ok(studyTableService.update(studyTableDTO));
    }

    @DeleteMapping("/{studyTableId}")
    @Operation(summary = "학습테이블 삭제", description = "학습테이블 아이디를 기반으로 학습테이블을 삭제합니다.")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("studyTableId") Long studyTableId) {
        studyTableService.delete(studyTableId);
        return ResponseEntity.ok(Map.of("result", "success"));
    }
}
