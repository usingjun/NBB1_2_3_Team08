package edu.example.learner_kotlin.token.service

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import edu.example.learner_kotlin.token.entity.RefreshEntity
import edu.example.learner_kotlin.token.exception.JWTException
import edu.example.learner_kotlin.token.repository.TokenRepository
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TokenService (
    private val tokenRepository: TokenRepository,
    private val jwtUtil: JWTUtil,
    private val redisTemplate: RedisTemplate<String, RefreshEntity>
){
    fun deleteRefreshToken(request: HttpServletRequest): Map<String, Any> {
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

        if (refreshToken == null) {
            throw JWTException.REFRESH_TOKEN_NOT_FOUND_COOKIE.jwtTaskException
        }

        //expired check
        try {
            jwtUtil.isExpired(refreshToken)
        } catch (e: ExpiredJwtException) {
            throw JWTException.REFRESH_TOKEN__EXPIRED.jwtTaskException
        }

        val claims = jwtUtil.validateToken(refreshToken)
        val category = claims["category"].toString()

        // Refresh 토큰인지 확인
        if (category != "refresh") {
            throw JWTException.CATEGORY_NOT_REFRESH.jwtTaskException
        }

        //DB에 저장되어 있는지 확인
        val isExist: Boolean = tokenRepository.existsRefreshEntityBy(refreshToken)
        if (!isExist) {
            //response body
            throw JWTException.REFRESH_TOKEN_NOT_FOUND.jwtTaskException
        } else {
            log.info("Refresh token exists")
        }

        //토큰 삭제
        try {
            val key = "token:$refreshToken"
            val result = redisTemplate.delete(key)
            if(result){
                log.info("Token refresh successfully deleted")
            } else{
                log.info("Token refresh remove failed")
            }
        }catch (e:Exception){
            throw JWTException.TOKEN_NOT_REMOVE.jwtTaskException
        }

        return claims
    }

    fun addRefreshEntity(username: String, refreshToken: String) {
        val refreshEntity = RefreshEntity(username = username, refreshToken = refreshToken)
        tokenRepository.save(refreshEntity)
    }
}