package edu.example.learner_kotlin.qna.answer.controller

import edu.example.learner_kotlin.qna.answer.dto.AnswerDTO
import edu.example.learner_kotlin.qna.answer.service.AnswerService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/answers")
class AnswerController(val answerService: AnswerService) {
    @GetMapping("/{inquiryId}")
    fun read(@PathVariable("inquiryId") inquiryID: Long) = ResponseEntity.ok(answerService.readByInquiryId(inquiryID))

    @GetMapping
    fun readAll() = ResponseEntity.ok(answerService.readAll())

    @PostMapping
    fun register(@Validated @RequestBody answerDTO: AnswerDTO) = ResponseEntity.ok(answerService.register(answerDTO))

    @PutMapping("/{answerId}")
    fun update(@PathVariable("answerId") answerId: Long, @Validated @RequestBody answerDTO: AnswerDTO) =
        ResponseEntity.ok(answerService.update(answerDTO.apply { this.answerId = answerId }))

    @DeleteMapping("/{inquiryId}")
    fun delete(@PathVariable("inquiryId") inquiryID: Long) =
        ResponseEntity.ok(answerService.deleteByInquiryId(inquiryID))

}