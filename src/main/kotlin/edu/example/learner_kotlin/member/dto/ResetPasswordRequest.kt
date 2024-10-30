package edu.example.learner_kotlin.member.dto

import jakarta.validation.constraints.NotBlank

data class ResetPasswordRequest(
    @field:NotBlank(message = "비밀번호는 필수입니다.")
    val newPassword: String
)
