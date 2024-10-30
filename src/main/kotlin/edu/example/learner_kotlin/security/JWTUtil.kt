package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Component
class JWTUtil(@Value("\${jwt.secret}") secretKey: String?) {
    private val key: SecretKey = run{
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }

    // JWT 문자열 생성 (저장 문자열, 만료 시간 - 분 단위)
    fun createToken(valueMap: Map<String?, Any?> , min: Int): String {
        val now = Date() // 토큰 발행 시간
        val headers = mapOf("alg" to "HS256", "typ" to "JWT") // 전체 헤더를 Map 으로 설정

        return Jwts.builder().apply {
            header().add(headers)                                               // 헤더 설정
            issuedAt(now)                                                       // 토큰 발행 시간
            expiration(Date(
                now.time + Duration.ofMinutes(min.toLong()).toMillis()))   // 토큰 만료 시간
            claims(valueMap)                                                    // 저장 데이터
            signWith(key)                                                       // 서명
        }.compact()
    }

    //검증 기능 수행
    fun validateToken(token: String?): Map<String, Any> {
        try {
            val claims: Claims = Jwts.parser()          //JWT 파싱하고 파서객체 생성
                .verifyWith(key)                        //JWT 를 서명하는데 사용된 비밀키 설정
                .build()                                //파서를 설정하여 빌드
                .parseClaimsJwt(token)                  //JWT 파싱 및 서명 검증
                .body                                   // 클레임 가져오기
            log.info("--- claims : $claims")
            return claims
        } catch (e: IllegalArgumentException) {
            log.error("Invalid JWT token: {}", e.message)
            throw RuntimeException("Invalid JWT token")
        }
    }
}
