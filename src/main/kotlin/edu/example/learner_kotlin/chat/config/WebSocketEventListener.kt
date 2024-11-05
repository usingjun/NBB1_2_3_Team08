package edu.example.learner_kotlin.chat.config

import edu.example.learner_kotlin.chat.dto.Message
import edu.example.learner_kotlin.chat.dto.MsgType
import edu.example.learner_kotlin.log
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent


@Component
class WebSocketEventListener(
    private val messageOperations: SimpMessageSendingOperations // STOMP 메시지를 보내는 데 사용되는 인터페이스
) {

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as String?

        if (username != null) {
            log.info("User Connected: $username")
            messageOperations.convertAndSend("/sub/chat", createJoinMessage(username))
        }
        log.info("WebSocket 연결이 열렸습니다: ${event.message}")
    }

    @EventListener // 이 이벤트는 WebSocket 세션이 종료될 때 발생합니다.
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        // StompHeaderAccessor는 STOMP 메시지의 헤더를 읽고 쓰는 데 사용되는 클래스입니다.
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as String?

        // 사용자 이름이 null이 아닌 경우 사용자가 채팅방을 나갔음을 로그에 기록합니다.
        if (username != null) {
            log.info("User Disconnected $username")

            // messageOperations.convertAndSend()를 사용하여 메시지를 전송
            // 사용자가 채팅방을 나갔음을 모든 클라이언트에게 알립니다.
            messageOperations.convertAndSend("/sub/chat", createLeaveMessage(username))
        }
    }


    private fun createJoinMessage(userName: String): Message {
        return Message(
            type = MsgType.JOIN,
            sender = userName,
            content = "$userName 님이 입장하셨습니다." // 입장 메시지 추가
        )
    }

    private fun createLeaveMessage(username: String): Message {
        return Message(
            type = MsgType.LEAVE,
            sender = username,
            content = ""
        )
    }
}