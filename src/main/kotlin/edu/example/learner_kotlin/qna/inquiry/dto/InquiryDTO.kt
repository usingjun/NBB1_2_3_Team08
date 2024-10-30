package edu.example.learner_kotlin.qna.inquiry.dto

import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import edu.example.learner_kotlin.qna.inquiry.entity.InquiryStatus
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class InquiryDTO(
    var inquiryId: Long? = null,

    @NotBlank
    var inquiryTitle: String? = null,

    @NotBlank
    var inquiryContent: String? = null,

    var inquiryCreateDate: LocalDateTime? = null,

    var inquiryUpdateDate: LocalDateTime? = null,

    var inquiryStatus: String? = InquiryStatus.CONFIRMING.name,

    @NotBlank
    var memberId: Long? = null,

    var memberNickname: String? = null,
) {
    constructor(inquiry: Inquiry) : this(
        inquiry.inquiryId,
        inquiry.inquiryTitle,
        inquiry.inquiryContent,
        inquiry.inquiryCreateDate,
        inquiry.inquiryUpdateDate,
        inquiry.inquiryStatus?.name,
        inquiry.member?.memberId,
        inquiry.member?.nickname,
    )
}