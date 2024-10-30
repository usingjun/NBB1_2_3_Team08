package edu.example.learner_kotlin.qna.answer.dto

import edu.example.learner_kotlin.qna.answer.entity.Answer
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class AnswerDTO(
    var answerId: Long? = null,

    @NotBlank
    var answerContent: String? = null,

    var answerCreateDate: LocalDateTime? = null,

    var inquiryId: Long? = null,
) {
    constructor(answer: Answer) : this(
        answer.answerId,
        answer.answerContent,
        answer.answerCreateDate,
        answer.inquiry?.inquiryId
    )
}