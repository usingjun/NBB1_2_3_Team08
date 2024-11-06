package edu.example.learner_kotlin.chat.interceptor


import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.security.Principal

@Component
// StompHandler.kt
class StompHandler(
    private val jwtTokenProvider: JWTUtil
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        when (accessor?.command) {
            StompCommand.CONNECT -> {
                // 원본 헤더에서 속성 가져오기
                val simpSession = message.headers[SimpMessageHeaderAccessor.SESSION_ATTRIBUTES] as? Map<*, *>
                val username = simpSession?.get("username") as? String

                log.info("STOMP CONNECT - Trying to get username from session: $username")

                // STOMP 세션에 username 저장
                if (username != null) {
                    accessor.sessionAttributes = accessor.sessionAttributes ?: mutableMapOf()
                    accessor.sessionAttributes!!["username"] = username
                    log.info("STOMP CONNECT - Username set in session: $username")
                }
            }
            StompCommand.SUBSCRIBE -> {
                val username = accessor.sessionAttributes?.get("username") as? String
                log.info("STOMP SUBSCRIBE - Current username in session: $username")
            }
            else -> {}
        }

        return message
    }
}

