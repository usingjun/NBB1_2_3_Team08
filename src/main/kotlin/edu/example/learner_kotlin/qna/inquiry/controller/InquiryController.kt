package edu.example.learner_kotlin.qna.inquiry.controller

import edu.example.learner_kotlin.qna.inquiry.dto.InquiryDTO
import edu.example.learner_kotlin.qna.inquiry.service.InquiryService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inquiries")
class InquiryController(private val inquiryService: InquiryService) {
    @GetMapping("/{inquiryId}")
    fun read(@PathVariable("inquiryId") inquiryId: Long) = ResponseEntity.ok(inquiryService.read(inquiryId))

    @GetMapping("/member/{memberId}")
    fun readByMemberId(@PathVariable("memberId") memberId: Long) =
        ResponseEntity.ok(inquiryService.readByMemberId(memberId))

    @GetMapping
    fun readAll() = ResponseEntity.ok(inquiryService.readAll())

    @PostMapping
    fun register(@Validated @RequestBody inquiryDTO: InquiryDTO) =
        ResponseEntity.ok(inquiryService.register(inquiryDTO))

    @PutMapping("/{inquiryId}")
    fun update(@PathVariable("inquiryId") inquiryId: Long, @Validated @RequestBody inquiryDTO: InquiryDTO) =
        ResponseEntity.ok(inquiryService.update(inquiryDTO.apply { this.inquiryId = inquiryId }))

    @PutMapping("/{inquiryId}/status")
    fun updateStatus(@PathVariable("inquiryId") inquiryId: Long, @RequestBody inquiryStatus: String) = run {
        val inquiryDTO = inquiryService.read(inquiryId).apply { this.inquiryStatus = inquiryStatus }
        ResponseEntity.ok(inquiryService.update(inquiryDTO))
    }

    @DeleteMapping("/{inquiryId}")
    fun delete(@PathVariable("inquiryId") inquiryId: Long) = ResponseEntity.ok(inquiryService.delete(inquiryId))
}