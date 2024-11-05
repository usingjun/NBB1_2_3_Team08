package edu.example.learner_kotlin.courseabout.news.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.Authentication

@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var jwtUtil: JWTUtil

    @GetMapping("/user-info")
    fun getUserInfo(authentication: Authentication?): ResponseEntity<Any> {
        if (authentication == null || !authentication.isAuthenticated) {
            return ResponseEntity.status(401).body("Unauthorized")
        }

        // 인증된 사용자 정보 가져오기 (role과 mid)
        val authorities = authentication.authorities.map { it.authority }
        val role = if (authorities.isNotEmpty()) authorities[0] else "ROLE_USER"
        val username = authentication.name // username이 mid일 경우

        log.info("role: $role, username: $username")
        return ResponseEntity.ok(
            mapOf(
                "role" to role,
                "mid" to username  // 또는 실제 사용자 ID가 필요하다면 다른 필드를 사용하세요.
            )
        )
    }
}