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
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "회원가입에 성공하였습니다."
        ), ApiResponse(
            responseCode = "404",
            description = "회원가입에 실패하였습니다.",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(example = "{\"error\": \"회원가입에 실패하였습니다.\"}")
            )]
        )]
    )
    fun memberRegister(@RequestBody @Validated memberDTO: MemberDTO): ResponseEntity<String> {
        log.info("--- memberRegister()")
        memberService.register(memberDTO)

        return ResponseEntity.status(HttpStatus.CREATED).body<String>("회원가입에 성공하셨습니다.")
    }

    //로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일, 비밀번호을 변수로 받아 로그인을 시도합니다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "로그인에 성공하였습니다."
        ), ApiResponse(
            responseCode = "404",
            description = "로그인에 실패하였습니다.",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(example = "{\"error\": \"로그인에 실패하였습니다.\"}")
            )]
        )]
    )
    @Throws(
        IOException::class
    )
    fun login(
        @RequestBody @Validated loginDTO: LoginDTO,
        response: HttpServletResponse
    ): ResponseEntity<MutableMap<String, Long?>> {
        log.info("--- login()")
        log.info("loginDTO: {}", loginDTO)

        val readInfo: LoginDTO = memberService.login(loginDTO.email, loginDTO.password)

        response.addCookie(readInfo.cookie)

        val responseBody = mutableMapOf<String, Long?>().apply {
            this["memberId"] = readInfo.memberId
        }

        return ResponseEntity.ok(responseBody)
    }
}


