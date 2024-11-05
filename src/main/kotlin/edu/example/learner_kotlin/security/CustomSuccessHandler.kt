package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.oauth2.CustomOauth2User
import edu.example.learner_kotlin.token.util.CookieUtil
import edu.example.learner_kotlin.token.util.TokenUtil
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomSuccessHandler(private val jwtUtil: JWTUtil,
                           private val cookieUtil: CookieUtil,
                           private val tokenUtil: TokenUtil) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        // OAuth2User
        val user: CustomOauth2User = authentication.principal as CustomOauth2User

        // 원본 claims을 복사하여 각각 별도로 사용
        val accessClaims = user.attributes.toMutableMap()
        val refreshClaims = user.attributes.toMutableMap()

        // 각 claims에 category 값 설정
        accessClaims["category"] = "access"
        refreshClaims["category"] = "refresh"

        // JWT 생성
        val accessToken: String = jwtUtil.createToken(accessClaims, 30) // 30분
        val refreshToken: String = jwtUtil.createToken(refreshClaims, 1440) // 24시간

        // Refresh 토큰 Redis에 저장
        tokenUtil.addRefreshEntity(accessClaims["username"].toString(), refreshToken)

        // Refresh 토큰을 쿠키에 저장
        response.addCookie(cookieUtil.createCookie("RefreshToken", refreshToken))

        // Redirect URL에 쿼리 파라미터 추가
        val memberId = accessClaims["mid"]
        val redirectUrl = "http://localhost:3000/courses?accessToken=$accessToken&memberId=$memberId"

        // 리다이렉션
        response.sendRedirect(redirectUrl)
    }

}
