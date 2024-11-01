package edu.example.learner_kotlin.courseabout.courseqna.courseanswer.dto

import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseAnswer
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import edu.example.learner_kotlin.member.entity.Member
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class CourseAnswerDTO(
    var answerId: Long? = null,

    @NotBlank
    var answerContent: String? = null,

    var answerCreateDate: LocalDateTime? = null,

    var inquiryId: Long? = null,
    var memberId: Long? = null,
    var memberNickname: String? = null,    // 작성자 닉네임
    var profileImage: ByteArray? = null    // 작성자 프로필 이미지
) {
    // CourseAnswer 엔티티를 DTO로 변환하는 보조 생성자
    constructor(answer: CourseAnswer) : this(
        answer.answerId,
        answer.answerContent,
        answer.answerCreateDate,
        answer.courseInquiry?.inquiryId,
        answer.member?.memberId,
        answer.member?.nickname,
        answer.member?.profileImage
    )

    fun toEntity(): CourseAnswer {
        return CourseAnswer(
            answerId = answerId,
            member = memberId?.let { Member(it) },
            answerContent = answerContent,
            answerCreateDate = answerCreateDate,
            courseInquiry = inquiryId?.let { CourseInquiry(it) }
        )
    }

    override fun toString(): String {
        return "CourseAnswerDTO(answerId=$answerId, answerContent=$answerContent, memberNickname=$memberNickname)"
    }
}
