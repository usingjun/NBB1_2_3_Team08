package edu.example.learner_kotlin.chat.interceptor


import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

@Component
class StompHandler(
    private val jwtTokenProvider: JWTUtil  // JWT 토큰을 검증하는 유틸리티 클래스 주입
) : ChannelInterceptor {

    // WebSocket 메시지가 전송되기 전에 호출되는 메서드
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        // STOMP 메시지에서 헤더 정보를 추출하기 위해 StompHeaderAccessor 사용
        val accessor = StompHeaderAccessor.wrap(message)

        // 메시지와 헤더 정보를 로그로 출력 (디버깅용)
        log.info("message: $message")
        log.info("헤더 : ${message.headers}")
        log.info("토큰: ${accessor.getNativeHeader("Authorization")}")

        // STOMP 명령어가 CONNECT일 때만 JWT 토큰 검증을 수행
        if (StompCommand.CONNECT == accessor.command) {
            // Authorization 헤더에서 JWT 토큰을 추출 (Bearer 접두사 제거)
            val token = accessor.getFirstNativeHeader("Authorization")?.substring(7)

            // 추출한 JWT 토큰을 검증 (null이 아닐 경우에만 검증)
            jwtTokenProvider.validateToken(token!!)
        }

        // 메시지를 그대로 반환하여 이후 로직에서 처리될 수 있도록 함
        return message
    }
}