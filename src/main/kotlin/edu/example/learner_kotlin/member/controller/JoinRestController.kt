package edu.example.learner_kotlin.member.controller

import edu.example.learner_kotlin.member.service.MemberService
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.LoginDTO
import edu.example.learner_kotlin.member.dto.MemberDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
@RequestMapping("/join")
@Tag(name = "로그인 및 회원가입 컨트롤러", description = "로그인 및 회원가입을 제공하는 API입니다.")
class JoinRestController (
    private val memberService: MemberService,
){

    //회원가입
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 전화번호, 닉네임을 변수로 받아 회원가입을 시도합니다.")
    fun memberRegister(@RequestBody @Validated memberDTO: MemberDTO): ResponseEntity<String> {
        log.info("--- memberRegister()")
        memberService.register(memberDTO)

        return ResponseEntity.status(HttpStatus.CREATED).body<String>("회원가입에 성공하셨습니다.")
    }

    //로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 시도합니다.")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<String> {
        log.info("--- logout()")
        // RefreshToken 쿠키 삭제
        val refreshTokenCookie = Cookie("RefreshToken", "").apply {
            maxAge = 0 // 쿠키 만료
            path = "/"
            isHttpOnly = true
            secure = false
        }
        response.addCookie(refreshTokenCookie)

        return ResponseEntity.status(HttpStatus.CREATED).body<String>("로그아웃에 성공하셨습니다.")
    }

}


