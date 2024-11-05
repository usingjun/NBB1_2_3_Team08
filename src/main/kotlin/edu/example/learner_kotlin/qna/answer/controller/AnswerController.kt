package edu.example.learner_kotlin.qna.answer.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.qna.answer.dto.AnswerDTO
import edu.example.learner_kotlin.qna.answer.service.AnswerService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/answers")
class AnswerController(val answerService: AnswerService) {
    @GetMapping("/{inquiryId}")
    fun read(@PathVariable("inquiryId") inquiryID: Long) = run {
        val answerDTO = answerService.readByInquiryId(inquiryID) ?: AnswerDTO()
        ResponseEntity.ok(answerDTO)
    }

    @GetMapping
    fun readAll() = ResponseEntity.ok(answerService.readAll())

    @PostMapping
    fun register(@Validated @RequestBody answerDTO: AnswerDTO) = ResponseEntity.ok(answerService.register(answerDTO))

    @PutMapping("/{inquiryId}")
    fun update(@PathVariable("inquiryId") inquiryId: Long, @Validated @RequestBody answerDTO: AnswerDTO) = run {
        val foundAnswerDTO = answerService.readByInquiryId(inquiryId) ?: throw NoSuchElementException("Answer not found")
        log.info("foundAnswerDTO: ${foundAnswerDTO.toString()}")
        foundAnswerDTO.apply { this?.answerContent = answerDTO.answerContent }
        log.info("changedAnswerDTO: ${foundAnswerDTO.toString()}")
        ResponseEntity.ok(answerService.update(foundAnswerDTO))

    }

    @DeleteMapping("/{inquiryId}")
    fun delete(@PathVariable("inquiryId") inquiryID: Long) =
        ResponseEntity.ok(answerService.deleteByInquiryId(inquiryID))

}