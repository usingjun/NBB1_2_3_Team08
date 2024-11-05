package edu.example.learner_kotlin.token.controller

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import io.jsonwebtoken.Claims
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(private val jwtUtil: JWTUtil) {
    @GetMapping("/decode")
    fun decodeToken(@RequestHeader("Authorization") authorizationHeader: String): Map<String, Any> {
        val token = authorizationHeader.replace("Bearer ", "")
        try {
            val claims: Claims? = jwtUtil.decodeToken(token)
            return mapOf("role" to claims?.get("role")!!)
        } catch (e: Exception) {
            log.error(e.message, e)
            throw e
        }
    }
}