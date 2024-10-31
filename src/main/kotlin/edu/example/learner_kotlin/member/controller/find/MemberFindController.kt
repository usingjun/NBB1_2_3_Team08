package edu.example.learner_kotlin.member.controller.find

import edu.example.learner_kotlin.member.dto.ResetPasswordRequest
import edu.example.learner_kotlin.member.dto.SendResetPasswordEmailReq
import edu.example.learner_kotlin.member.dto.SendResetPasswordEmailRes
import edu.example.learner_kotlin.member.service.find.LoginService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/members/find")
@Tag(name = "회원 찾기", description = "회원을 찾는 API")
class MemberFindController(
    private val loginService: LoginService
) {

    // 전화번호로 이메일(아이디) 찾기
    @GetMapping("/emails")
    @Operation(summary = "전화번호로 이메일(아이디) 찾기", description = "전화번호로 가입된 회원의 이메일(아이디)을 찾습니다.")
    fun findEmailByPhoneNumber(@RequestParam phoneNumber: String): ResponseEntity<List<String>> {
        val emails = loginService.findEmailsByPhoneNumber(phoneNumber)
        return if (emails.isEmpty()) {
            ResponseEntity.notFound().build() // 404 응답
        } else {
            ResponseEntity.ok(emails) // 200 응답
        }
    }

    @PostMapping("/send-reset-password")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호를 찾기 위한 이메일을 전송합니다.")
    fun sendResetPassword(@Validated @RequestBody req: SendResetPasswordEmailReq): SendResetPasswordEmailRes {
        loginService.checkMemberByEmail(req.email)
        val uuid = loginService.sendResetPasswordEmail(req.email)
        return SendResetPasswordEmailRes(uuid = uuid)
    }

    @PostMapping("/reset-password/{uuid}")
    @Operation(summary = "비밀번호 재설정", description = "UUID로 비밀번호를 재설정합니다.")
    fun resetPassword(
        @PathVariable uuid: String,
        @Validated @RequestBody req: ResetPasswordRequest
    ): ResponseEntity<String> {
        loginService.resetPassword(uuid, req.newPassword)
        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.")
    }
}