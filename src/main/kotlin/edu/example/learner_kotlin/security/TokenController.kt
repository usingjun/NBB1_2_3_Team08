package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(private val jwtUtil: JWTUtil) {
    @GetMapping("/decode")
    fun decodeToken(@RequestHeader("Authorization") authorizationHeader: String): Map<String, Any> {
        log.info("Decoding start")
        val token = authorizationHeader.replace("Bearer ", "")
        log.info("Token: $token")
        val claims = jwtUtil.decodeToken(token) ?: throw NoSuchElementException("invalid token")
        val map = HashMap<String, Any>()
        log.info("CLAIMS: ${claims.toString()}")
        return mapOf("mid" to claims["mid"]!!, "role" to claims["role"]!!, "username" to claims["username"]!!)
    }
}