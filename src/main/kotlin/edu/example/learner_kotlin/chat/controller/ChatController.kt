package edu.example.learner_kotlin.chat.controller

import edu.example.learner_kotlin.chat.dto.Message
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Controller
@RequestMapping("/api/chat")
class ChatController(
    private val messagingTemplate: SimpMessagingTemplate // SimpMessagingTemplate을 사용하여 메시지를 클라이언트에게 전달
) {

    @MessageMapping("/chat/enter")
    fun sendMessage(@RequestBody msg: Message): Message {
        messagingTemplate.convertAndSend("/sub/chat", msg)
        return msg // 받은 메시지를 그대로 반환하여 모든 구독자에게 전달합니다.
    }

    @MessageMapping("/chat/message")
    fun addUser(@RequestBody msg: Message): Message {
        // 사용자 이름을 세션 속성에 저장하는 로직을 추가해야 합니다.
        messagingTemplate.convertAndSend("/sub/chat", msg)
        return msg // 받은 메시지를 그대로 반환하여 새 사용자 입장을 알립니다.
    }

}