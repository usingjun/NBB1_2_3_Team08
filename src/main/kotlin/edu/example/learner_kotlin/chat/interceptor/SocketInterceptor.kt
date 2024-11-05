package edu.example.learner_kotlin.chat.interceptor

import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.dto.MemberDTO
import jakarta.security.auth.message.AuthException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Component
class SocketInterceptor : HandshakeInterceptor {

    @Throws(Exception::class)
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val req = (request as? ServletServerHttpRequest)?.servletRequest as? HttpServletRequest
        val session: HttpSession? = req?.getSession(false)

        log.info("Session >> $session")
        session?.getAttribute("user_info")?.let { userInfo ->
            val dto = userInfo as? MemberDTO
            val userId = dto?.memberId
            if (userId != null) {
                attributes["user_id"] = userId
                return true
            }
        }

        throw AuthException("로그인 해주세요")
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        TODO("Not yet implemented")
    }

}