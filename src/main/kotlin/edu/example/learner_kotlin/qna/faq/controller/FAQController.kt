package edu.example.learner_kotlin.qna.faq.controller

import edu.example.learner_kotlin.qna.faq.dto.FAQDTO
import edu.example.learner_kotlin.qna.faq.entity.FAQCategory
import edu.example.learner_kotlin.qna.faq.service.FAQService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/faqs")
class FAQController(val faqService: FAQService) {
    @GetMapping("/{faqId}")
    fun read(@PathVariable("faqId") faqId: Long) = ResponseEntity.ok(faqService.read(faqId))

    @GetMapping
    fun readAll() = ResponseEntity.ok(faqService.readAll())

    @GetMapping("/category/{category}")
    fun readByCategory(@PathVariable("category") category: String) = ResponseEntity.ok(
        faqService.readByCategory(
            FAQCategory.valueOf(category.uppercase(Locale.getDefault()))
        )
    )

    @PostMapping
    fun create(@Validated @RequestBody faqDTO: FAQDTO) = ResponseEntity.ok(faqService.register(faqDTO))

    @PutMapping("/{faqId}")
    fun update(@PathVariable("faqId") faqId: Long, @Validated @RequestBody faqDTO: FAQDTO) =
        ResponseEntity.ok(faqService.update(faqDTO.apply { this.faqId = faqId }))

    @DeleteMapping("/{faqId}")
    fun delete(@PathVariable("faqId") faqId: Long) = ResponseEntity.ok(faqService.delete(faqId))
}