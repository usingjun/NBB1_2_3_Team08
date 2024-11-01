package edu.example.learner_kotlin.member.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
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
class ReissueController(private val jwtUtil: JWTUtil) {
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

        log.info("--- refreshToken : $refreshToken")


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

        if (category != "refresh") {
            //response status code
            return ResponseEntity("invalid refresh token", HttpStatus.BAD_REQUEST)
        }

        //make new JWT
        val accessToken: String = jwtUtil.createToken(
            mutableMapOf("category" to "access", "username" to username, "role" to role), 30
        ) // 30분

        // response
        // Access 토큰을 JSON 응답 본문에 추가
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write("""{ "NewAccessToken": "$accessToken" }""")

        return ResponseEntity<Any>(HttpStatus.OK)
    }
}