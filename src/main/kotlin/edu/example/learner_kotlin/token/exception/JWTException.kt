package edu.example.learner_kotlin.token.exception

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus

enum class JWTException(private val message: String, private val status: Int) {
    ACCESS_TOKEN__EXPIRED("ACCESS Token이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    REFRESH_TOKEN__EXPIRED("REFRESH Token이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    ACCESS_TOKEN_NOT_FOUND_COOKIE("쿠키에서 Access Token을 발견하지 못했습니다.", HttpStatus.BAD_REQUEST.value()),
    REFRESH_TOKEN_NOT_FOUND_COOKIE("쿠키에서 Refresh Token을 발견하지 못했습니다.", HttpStatus.BAD_REQUEST.value()),
    JWT_AUTH_FAILURE("JWT 인증 실패", HttpServletResponse.SC_UNAUTHORIZED),
    CATEGORY_NOT_REFRESH("카테고리가 Refresh가 아닙니다.", HttpStatus.BAD_REQUEST.value()),
    ACCESS_TOKEN_NOT_FOUND("Access Token을 발견하지 못했습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("Refresh Token을 발견하지 못했습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    TOKEN_NOT_REMOVE("Token을 삭제하는데 실패했습니다.", HttpServletResponse.SC_UNAUTHORIZED);



    val jwtTaskException : JWTTaskException
        get() = JWTTaskException(this.message, status)
}
