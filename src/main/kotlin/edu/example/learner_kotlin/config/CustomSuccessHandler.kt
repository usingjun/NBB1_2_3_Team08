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

        val token: String = jwtUtil.createToken(claims, 30)
        log.info("token: $token")

        response.addCookie(createCookie("Authorization", token))

        // memberId를 가져와서 리디렉션 URL에 추가
        val memberId: Long? = user.memberId
        val redirectUrl = "http://localhost:3000/courses?memberId=$memberId"

        response.sendRedirect(redirectUrl)
    }

    fun createCookie(key: String?, value: String?): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60 * 60 * 60            // 60시간
        cookie.path = "/"                       // 전체 경로에서 접근 가능
        cookie.isHttpOnly = false               // JavaScript에서 접근 가능
        cookie.secure = false                   // 로컬 개발 시 false로 설정

        return cookie
    }
}
