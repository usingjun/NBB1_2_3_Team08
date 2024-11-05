package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.member.entity.Role
import edu.example.learner_kotlin.token.exception.JWTException
import edu.example.learner_kotlin.token.exception.JWTTaskException
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
        log.info("--- requestURI : " + request.requestURI)


        var isPublicPath = false

        // 요청 URI 및 HTTP 메소드 확인
        val requestURI: String = request.requestURI
        val method: String = request.method
        log.info("method : $method")

        // URL 디코딩
        val decodedURI: String = URLDecoder.decode(requestURI, StandardCharsets.UTF_8)
        log.info("decodedURI : $decodedURI")

        if ((request.method == "GET" && ((requestURI.matches("/course/\\d+".toRegex()) ||
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
                    requestURI.matches("/answers/\\d".toRegex()) ||
                    requestURI.matches("/attendances/\\d+/today".toRegex()) ||
                    requestURI.startsWith("/images") ||
                    requestURI.matches("/swagger-ui(/.*)?".toRegex()) || // swagger-ui로 시작하는 요청 및 favicon 파일 포함
                    requestURI.matches("/v3/api-docs(/.*)?".toRegex()) // v3/api-docs로 시작하는 요청
                    ))) || (request.method == "POST" &&
                    (requestURI.matches("/join/.*".toRegex()) || requestURI.matches("/members/find/.*".toRegex()) ||
                            requestURI.matches("/login.*".toRegex()) || requestURI.matches("/reissue".toRegex())))
        ) {
            log.info("JWT check passed $decodedURI")
            isPublicPath = true
        }

        if (isPublicPath) {
            filterChain.doFilter(request, response)
            return
        }

        // Authorization 헤더에서 accessToken 추출
        val authorizationHeader = request.getHeader("Authorization")
        log.info("Authorization header : $authorizationHeader")
        val accessToken = authorizationHeader?.removePrefix("Bearer ")

        // 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }


        try {
            // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
            try {
                jwtUtil.isExpired(accessToken)
                log.info("토큰 만료 인증 완료")
            } catch (e: ExpiredJwtException) {
                // 만료 예외 발생 시 바로 응답을 설정하고 종료
                handleException(response, JWTException.ACCESS_TOKEN__EXPIRED.jwtTaskException)
                return
            }

            log.info("--- 토큰 유효성 검증 시작 ---")
            val claims = jwtUtil.validateToken(accessToken)
            log.info("--- 토큰 유효성 검증 완료 ---")

            // SecurityContext 처리
            val mid = claims["username"].toString()
            val role = claims["role"].toString() // 단일 역할 처리
            val category = claims["category"].toString()

            //카테고리가 access가 아니라면 401오류 전달
            if (category != "access") {
                // response body
                val writer = response.writer
                writer.print("invalid access token")

                // response status code
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                return
            }

            fun convertRole(role: String): Role? {
                return try {
                    Role.valueOf(role.split("_")[1]) // 문자열을 대문자로 변환하여 enum과 일치시키기
                } catch (e: IllegalArgumentException) {
                    null // 유효하지 않은 문자열인 경우 null 반환
                }
            }

            val customUserPrincipal = CustomUserPrincipal(Member(nickname = mid, role = convertRole(role)))

            log.info(claims.toString())
            log.info("customUserPrincipal : ${customUserPrincipal.authorities}")
            // 토큰을 이용하여 인증된 정보 저장
            val authToken = UsernamePasswordAuthenticationToken(
                customUserPrincipal, null, customUserPrincipal.authorities
            )

            log.info("authToken : $authToken")

            // SecurityContext에 인증/인가 정보 저장
            log.info("SecurityContext에 인증/인가 정보 저장")
            SecurityContextHolder.getContext().apply{
                authentication = (authToken)
            }

            // OAuth2 인증 생략, 다음 필터로 요청 전달
            filterChain.doFilter(request, response)
            log.info("다음 필터로 요청 전달")
        } catch (e: Exception) {
            handleException(response, JWTException.JWT_AUTH_FAILURE.jwtTaskException) // 예외 발생 시 처리
        }
    }


    @Throws(IOException::class)
    fun handleException(response: HttpServletResponse, e: JWTTaskException) {
        log.info("--- handleException --- ${e.message}")
        response.apply {
            status = e.statusCode
            contentType = "application/json"
            writer.write("""{"error": "${e.message}"}""")
        }
    }
}
