package edu.example.learner_kotlin.security.exception

import jakarta.servlet.http.HttpServletResponse

enum class JWTException(private val message: String, private val status: Int) {
    ACCESS_TOKEN__EXPIRED("ACCESS Token이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    REFRESH_TOKEN__EXPIRED("REFRESH Token이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    ACCESS_TOKEN_NOT_FOUND("Access Token을 발견하지 못했습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("Refresh Token을 발견하지 못했습니다.", HttpServletResponse.SC_UNAUTHORIZED),
    JWT_AUTH_FAILURE("JWT 인증 실패", HttpServletResponse.SC_UNAUTHORIZED);



    val jwtTaskException : JWTTaskException
        get() = JWTTaskException(this.message, status)
}
