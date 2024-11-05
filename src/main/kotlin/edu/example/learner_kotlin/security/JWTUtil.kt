package edu.example.learner_kotlin.security

import edu.example.learner_kotlin.log
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SignatureException
import java.time.Duration
import java.util.*
import javax.crypto.SecretKey

@Component
class JWTUtil(@Value("\${jwt.secret}") secretKey: String?) {
    private val key: SecretKey = run{
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        Keys.hmacShaKeyFor(keyBytes)
    }

    fun isExpired(token: String?): Boolean {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.expiration
            .before(Date())
    }

    // JWT 문자열 생성 (저장 문자열, 만료 시간 - 분 단위)
    fun createToken(valueMap: MutableMap<String?, Any?> , min: Int): String {
        val now = Date() // 토큰 발행 시간
        val headers = mapOf("alg" to "HS256", "typ" to "JWT") // 전체 헤더를 Map 으로 설정

        return Jwts.builder().apply {
            header().add(headers)                                               // 헤더 설정
            issuedAt(now)                                                       // 토큰 발행 시간
            expiration(Date(
                now.time + Duration.ofMinutes(min.toLong()).toMillis()*60))   // 토큰 만료 시간
            claims(valueMap)                                                    // 저장 데이터
            signWith(key)                                                       // 서명
        }.compact()
    }

    fun validateToken(token: String?): Map<String, Any> {
        if (token.isNullOrBlank()) {
            log.error("JWT token is null or empty")
            throw IllegalArgumentException("JWT token cannot be null or empty")
        }

        return try {
            // JWT를 파싱하고 검증
            val jws: Jws<Claims> = Jwts.parser()
                .verifyWith(key) // 서명 검증 키 설정
                .build()
                .parseSignedClaims(token) // JWT 파싱 및 서명 검증

            // Claims 객체 가져오기
            val claims: Claims = jws.body

            log.info("--- claims : $claims")
            claims
        } catch (e: UnsupportedJwtException) {
            log.error("Unsupported JWT token: {}", e.message)
            throw RuntimeException("Unsupported JWT token")
        } catch (e: MalformedJwtException) {
            log.error("Malformed JWT token: {}", e.message)
            throw RuntimeException("Malformed JWT token")
        } catch (e: SignatureException) {
            log.error("JWT signature validation failed: {}", e.message)
            throw RuntimeException("JWT signature validation failed")
        } catch (e: SecurityException) {
            log.error("JWT string is actually a JWE: {}", e.message)
            throw RuntimeException("JWT string is actually a JWE")
        }catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: {}", e.message)
            throw RuntimeException("JWT token is expired")
        }
        catch (e: IllegalArgumentException) {
            log.error("Invalid JWT token: {}", e.message)
            throw RuntimeException("Invalid JWT token")
        }

    }

    fun decodeToken(token: String): Claims? = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload
}
