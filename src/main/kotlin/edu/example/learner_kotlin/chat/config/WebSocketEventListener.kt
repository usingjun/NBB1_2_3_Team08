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
        val accessor = StompHeaderAccessor.wrap(event.message)
        log.info("Connected Event - All Headers: ${accessor.messageHeaders}")
        log.info("Connected Event - Session Attributes: ${accessor.sessionAttributes}")

        val username = accessor.sessionAttributes?.get("username") as? String
        log.info("Connected Event - Username from session: $username")

        if (username != null) {
            messageOperations.convertAndSend(
                "/sub/chat",
                createJoinMessage(username)
            )
        } else {
            log.warn("Connected Event - No username found in session")
        }
    }

    @EventListener
    fun handleSessionSubscribeEvent(event: SessionSubscribeEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as? String

        if (username != null) {
            log.info("Subscribe event - Username: $username")
            messageOperations.convertAndSend(
                "/sub/chat",
                createJoinMessage(username)
            )
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
