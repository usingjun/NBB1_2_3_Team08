package edu.example.learner_kotlin.qna.faq.dto

import edu.example.learner_kotlin.qna.faq.entity.FAQ
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class FAQDTO(
    var faqId: Long? = null,

    @NotBlank
    var faqTitle: String? = null,

    @NotBlank
    var faqContent: String? = null,

    var faqCreateDate: LocalDateTime? = null,

    var faqUpdateDate: LocalDateTime? = null,

    @NotBlank
    var faqCategory: String? = null,
) {
    constructor(faq: FAQ) : this(
        faq.faqId,
        faq.faqTitle,
        faq.faqContent,
        faq.faqCreateDate,
        faq.faqUpdateDate,
        faq.faqCategory?.name
    )
}