package edu.example.learner_kotlin.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SendResetPasswordEmailReq(
    @field:NotBlank(message = "이메일은 필수 항목입니다.")
    @field:Email(message = "유효한 이메일 형식을 입력하세요.")
    val email: String
)
