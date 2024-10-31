package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class JWTCheckFilter(
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.info("--- doFilterInternal() ")
        log.info("--- requestURI : " + request.requestURI)


        var isPublicPath = false

        // 요청 URI 및 HTTP 메소드 확인
        val requestURI: String = request.requestURI
        val method: String = request.method
        log.info("method : $method")
        log.info("requestURI : $requestURI")

        // URL 디코딩
        val decodedURI: String = URLDecoder.decode(requestURI, StandardCharsets.UTF_8)
        log.info("decodedURI : $decodedURI")

        if ((request.getMethod() == "GET" && ((requestURI.matches("/course/\\d+".toRegex()) ||
                    requestURI.matches("/course/\\d+/member-nickname".toRegex()) ||
                    requestURI.matches("/course/video/\\d+".toRegex()) ||
                    requestURI.matches("/course/list".toRegex()) ||
                    decodedURI.matches("/members/other/[\\w가-힣]+".toRegex()) ||
                    decodedURI.matches("/members/instructor/[\\w가-힣]+".toRegex()) ||
                    requestURI.matches("/course/\\d+/news/\\d+".toRegex()) ||
                    requestURI.matches("/course/\\d+/news".toRegex()) ||
                    requestURI.matches("/members/find/.*".toRegex()) ||
                    decodedURI.matches("/members/instructor/[\\w가-힣]+/reviews/list".toRegex()) ||
                    requestURI.matches("/inquiries".toRegex()) ||
                    requestURI.matches("/course/\\d+/reviews/list".toRegex()) ||
                    requestURI.matches("/course/\\d+/course-inquiry/\\d+".toRegex()) ||
                    requestURI.matches("/course/\\d+/course-inquiry".toRegex()) ||
                    requestURI.matches("/course/\\d+/course-answer/\\d+".toRegex()) ||
                    requestURI.startsWith("/images")
                    ))) || (request.method == "POST" &&
                    (requestURI.matches("/join/.*".toRegex()) || requestURI.matches("/members/find/.*".toRegex())))
        ) {
            log.info("JWT check passed $decodedURI")
            isPublicPath = true
        }

        if (isPublicPath) {
            filterChain.doFilter(request, response)
            return
        }


        // Authorization 쿠키에서 토큰 추출
        var authorization: String? = null
        val cookies: Array<Cookie> = request.cookies
        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                log.info("cookie : " + cookie.name)
                if (cookie.name == "Authorization") {
                    authorization = cookie.value
                }
            }
        } else {
            log.info("--- No cookies found")
        }

        log.info("--- authorization : $authorization")

        if (authorization == null) {
            handleException(response, Exception("ACCESS TOKEN NOT FOUND"))
            return
        }

        // 토큰 유효성 검증
        val accessToken: String = authorization

        try {
            log.info("--- 토큰 유효성 검증 시작 ---")
            val claims = jwtUtil.validateToken(accessToken)
            log.info("--- 토큰 유효성 검증 완료 ---")

            // SecurityContext 처리
            val mid = claims["mid"].toString()
            val role = claims["role"].toString() // 단일 역할 처리

            log.info(claims.toString())
            log.info("권한 : $role")
            // 토큰을 이용하여 인증된 정보 저장
            val authToken = UsernamePasswordAuthenticationToken(
                CustomUserPrincipal(mid, role), null, mutableListOf(
                    SimpleGrantedAuthority(
                        "ROLE_$role"
                    )
                )
            )

            log.info("authToken : $authToken")

            // SecurityContext에 인증/인가 정보 저장
            SecurityContextHolder.getContext().apply{
                authentication = (authToken)
            }

            log.info("SecurityContext에 인증/인가 정보 저장")

            // OAuth2 인증 생략, 다음 필터로 요청 전달
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            handleException(response, e) // 예외 발생 시 처리
        }
    }


    @Throws(IOException::class)
    fun handleException(response: HttpServletResponse, e: Exception) {
        log.info("--- handleException ---")
        response.apply {
            status = HttpServletResponse.SC_FORBIDDEN
            contentType = "application/json"
            writer.write(e.message ?: "")
        }
    }
}
