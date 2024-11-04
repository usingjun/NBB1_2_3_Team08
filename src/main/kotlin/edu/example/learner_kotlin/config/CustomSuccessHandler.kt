package edu.example.learner_kotlin.config

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.oauth2.CustomOauth2User
import edu.example.learner_kotlin.security.JWTUtil
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomSuccessHandler(private val jwtUtil: JWTUtil) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // OAuth2User
        val user: CustomOauth2User = authentication.principal as CustomOauth2User
        val claims: MutableMap<String?, Any?> = user.attributes

        log.info(claims.toString())

        // JWT 생성
        val accessToken: String = jwtUtil.createToken(claims, 30) // 30분
        val refreshToken: String = jwtUtil.createToken(claims, 1440) // 24시간

        // Refresh 토큰을 쿠키에 저장
        response.addCookie(createCookie("RefreshToken", refreshToken))

        // Redirect URL에 쿼리 파라미터 추가
        val memberId = claims["mid"]
        val redirectUrl = "http://localhost:3000/courses?accessToken=$accessToken&memberId=$memberId"

        // 리다이렉션
        response.sendRedirect(redirectUrl)
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60
        cookie.secure = false;
        cookie.path = "/";
        cookie.isHttpOnly = true

        return cookie
    }
}
