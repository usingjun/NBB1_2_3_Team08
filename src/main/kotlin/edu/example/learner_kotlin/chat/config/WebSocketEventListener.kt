package edu.example.learner_kotlin.chat.config

import edu.example.learner_kotlin.chat.dto.Message
import edu.example.learner_kotlin.chat.dto.MsgType
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.security.JWTUtil
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent

@Component
class WebSocketEventListener(
    private val messageOperations: SimpMessageSendingOperations,
    private val jwtTokenProvider: JWTUtil
) {

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        log.info("Connected - All Headers: ${headerAccessor.messageHeaders}")

        // 세션 속성에서 토큰 가져오기
        val token = headerAccessor.sessionAttributes?.get("token") as? String

        if (!token.isNullOrEmpty()) {
            try {
                val claims = jwtTokenProvider.validateToken(token)
                val username = claims["username"] as String?

                if (username != null) {
                    log.info("User Connected: $username")
                    // 사용자 정보를 세션에 저장
                    headerAccessor.sessionAttributes?.put("username", username)

                    // 입장 메시지 전송
                    messageOperations.convertAndSend(
                        "/sub/chat",  // topic 대신 sub 사용
                        createJoinMessage(username)
                    )
                }
            } catch (e: Exception) {
                log.error("Token validation failed: ${e.message}")
            }
        } else {
            log.warn("No token found in session attributes")
        }
    }

    @EventListener
    fun handleSessionSubscribeEvent(event: SessionSubscribeEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as? String

        if (username != null) {
            log.info("Subscribe event - Username: $username")
            log.info("Subscribe destination: ${headerAccessor.destination}")
        } else {
            log.warn("Subscribe event - No username found in session")
        }
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as? String

        if (username != null) {
            log.info("User Disconnected: $username")
            log.info("Disconnect reason: ${event.closeStatus}")

            messageOperations.convertAndSend(
                "/sub/chat",  // topic 대신 sub 사용
                createLeaveMessage(username)
            )
        } else {
            log.warn("Disconnected - No username found in session")
        }
    }

    private fun createJoinMessage(username: String): Message {
        return Message(
            type = MsgType.JOIN,
            sender = username,
            content = "$username 님이 입장하셨습니다.",
            timestamp = System.currentTimeMillis()
        )
    }

    private fun createLeaveMessage(username: String): Message {
        return Message(
            type = MsgType.LEAVE,
            sender = username,
            content = "$username 님이 퇴장하셨습니다.",
            timestamp = System.currentTimeMillis()
        )
    }

    private fun createErrorMessage(username: String, error: String): Message {
        return Message(
            type = MsgType.ERROR,
            sender = username,
            content = error,
            timestamp = System.currentTimeMillis()
        )
    }
}
