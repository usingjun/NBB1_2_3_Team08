package edu.example.learner_kotlin.chat.interceptor

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.stereotype.Component
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.socket.WebSocketHandler

@Component
class JwtHandshakeInterceptor(
    private val jwtTokenProvider: JWTUtil
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        if (request is ServletServerHttpRequest) {
            val token = request.servletRequest.getParameter("token")
                ?: return false.also { log.warn("토큰이 없습니다") }

            try {
                if (jwtTokenProvider.isExpired(token)) {
                    log.warn("토큰이 만료되었습니다")
                    return false
                }

                val claims = jwtTokenProvider.validateToken(token)
                val username = claims["username"] as String?
                    ?: return false.also { log.warn("사용자 정보가 없습니다") }

                // 토큰과 사용자 정보를 세션 속성에 저장
                attributes["token"] = token
                attributes["username"] = username
                return true
            } catch (e: Exception) {
                log.error("토큰 검증 실패: ${e.message}")
                return false
            }
        }
        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // 핸드셰이크 후 처리는 필요 없음
    }
}
