package edu.example.learner_kotlin.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(private val authenticationManager: AuthenticationManager,
                  private val jwtUtil: JWTUtil) :
    UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val username = request.getParameter("username") // 올바른 파라미터 이름인지 확인
        val password = request.getParameter("password") // 올바른 파라미터 이름인지 확인

        println(username)

        val authToken: UsernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(username, password, null)

        return authenticationManager.authenticate(authToken)
    }

    protected override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val customUserPrincipal: CustomUserPrincipal = authentication.principal as CustomUserPrincipal

        val username: String? = customUserPrincipal.username
        val memberId: Long? = customUserPrincipal.getMemberId()

        val authorities = authentication.authorities
        val iterator: Iterator<GrantedAuthority> = authorities.iterator()
        val auth = iterator.next()
        val role = auth.authority

        // JWT 생성
        val accessToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "access", "username" to username, "role" to role), 30
        ) // 30분
        val refreshToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "refresh", "username" to username, "role" to role), 1440
        ) // 24시간

        // Refresh 토큰을 쿠키에 저장
        response.addCookie(createCookie("RefreshToken", refreshToken))

        // Access 토큰을 JSON 응답 본문에 추가
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{ "accessToken": "$accessToken", "memberId": $memberId }""")
    }


    protected override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.status = 401
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60
        cookie.secure = true;
        cookie.path = "/";
        cookie.isHttpOnly = true

        return cookie
    }
}
