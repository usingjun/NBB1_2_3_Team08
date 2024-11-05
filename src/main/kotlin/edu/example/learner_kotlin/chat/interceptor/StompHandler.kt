package edu.example.learner_kotlin.chat.interceptor


import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import java.security.Principal

@Component
// StompHandler.kt
class StompHandler(
    private val jwtTokenProvider: JWTUtil
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)
            ?: return message

        if (StompCommand.CONNECT == accessor.command) {
            val sessionAttributes = accessor.sessionAttributes ?: return message
            val username = sessionAttributes["username"] as? String
                ?: throw Exception("인증 정보가 없습니다")

            // Principal 설정
            accessor.user = CustomPrincipal(username)
        }
        return message
    }
}

// 사용자 정보를 담을 Principal 구현
class CustomPrincipal(private val name: String) : Principal {
    override fun getName(): String = name
}
