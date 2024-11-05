package edu.example.learner_kotlin.token.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import edu.example.learner_kotlin.token.entity.RefreshEntity
import edu.example.learner_kotlin.token.repository.TokenRepository
import edu.example.learner_kotlin.token.util.CookieUtil
import edu.example.learner_kotlin.token.util.TokenUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@ResponseBody
class ReissueController(private val jwtUtil: JWTUtil,
                        private val tokenRepository: TokenRepository,
                        private val cookieUtil: CookieUtil,
                        private val tokenUtil: TokenUtil,) {
    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {

        //get refresh token
        var refreshToken: String? = null
        val cookies: Array<Cookie> = request.cookies
        if (cookies.isNotEmpty()) {
            for (cookie in cookies) {
                log.info("cookie : " + cookie.name)
                if (cookie.name == "RefreshToken") {
                    refreshToken = cookie.value
                }
            }
        } else {
            log.info("--- No cookies found")
        }

        log.info("--- New RefreshToken : $refreshToken")


        if (refreshToken == null) {
            //response status code
            return ResponseEntity("refresh token null", HttpStatus.BAD_REQUEST)
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken)
        } catch (e: ExpiredJwtException) {
            //response status code
            return ResponseEntity("refresh token expired", HttpStatus.BAD_REQUEST)
        }

        val claims = jwtUtil.validateToken(refreshToken)
        val username = claims["username"].toString()
        val role = claims["role"].toString() // 단일 역할 처리
        val category = claims["category"].toString()

        // Refresh 토큰인지 확인
        if (category != "refresh") {
            //response status code
            return ResponseEntity("invalid refresh token", HttpStatus.BAD_REQUEST)
        }


        //DB에 저장되어 있는지 확인
        val isExist: Boolean = tokenRepository.existsRefreshEntityBy(refreshToken)
        if (!isExist) {
            //response body
            return ResponseEntity("invalid refresh token", HttpStatus.BAD_REQUEST)
        }

        //make new JWT
        val newAccessToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "access", "username" to username, "role" to role, "mid" to claims["mid"].toString()), 30
        ) // 30분
        val newRefreshToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "refresh", "username" to username, "role" to role, "mid" to claims["mid"].toString()), 1440
        ) // 24시간

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        tokenRepository.deleteRefreshEntityBy(refreshToken);
        tokenUtil.addRefreshEntity(username, newRefreshToken);

        // Refresh 토큰을 쿠키에 저장
        response.addCookie(cookieUtil.createCookie("RefreshToken", newRefreshToken))

        // Access 토큰을 JSON 응답 본문에 추가
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{ "accessToken": "$newAccessToken" }""")

        return ResponseEntity<Any>(HttpStatus.OK)
    }
}