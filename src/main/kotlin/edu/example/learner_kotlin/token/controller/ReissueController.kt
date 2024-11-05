package edu.example.learner_kotlin.token.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import edu.example.learner_kotlin.token.service.TokenService
import edu.example.learner_kotlin.token.util.CookieUtil
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
                        private val cookieUtil: CookieUtil,
                        private val tokenService: TokenService
) {
    @PostMapping("/reissue")
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<*> {

        val claims = tokenService.deleteRefreshToken(request)
        val username = claims["username"].toString()
        val role = claims["role"].toString() // 단일 역할 처리

        //make new JWT
        val newAccessToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "access", "username" to username, "role" to role, "mid" to claims["mid"].toString()), 30
        ) // 30분
        val newRefreshToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "refresh", "username" to username, "role" to role, "mid" to claims["mid"].toString()), 1440
        ) // 24시간

        log.info("--- New Access Token : $newAccessToken")
        log.info("--- New RefreshToken : $newRefreshToken")

        tokenService.addRefreshEntity(username, newRefreshToken);

        // Refresh 토큰을 쿠키에 저장
        response.addCookie(cookieUtil.createCookie("RefreshToken", newRefreshToken))

        // Access 토큰을 JSON 응답 본문에 추가
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{ "accessToken": "$newAccessToken" }""")

        return ResponseEntity<Any>(HttpStatus.OK)
    }
}