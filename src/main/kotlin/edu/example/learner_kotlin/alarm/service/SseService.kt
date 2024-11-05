package edu.example.learner_kotlin.alarm.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import edu.example.learner_kotlin.log
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class SseService {
    private val emitters = ConcurrentHashMap<Long, SseEmitter>()
    private val objectMapper = jacksonObjectMapper()

    // SseEmitter 생성 로직을 별도의 메소드로 분리
    private fun createEmitter(memberId: Long): SseEmitter {
        val emitter = SseEmitter(Long.MAX_VALUE) // 무제한 타임아웃 설정
        emitter.onCompletion { removeEmitter(memberId) }
        emitter.onTimeout { removeEmitter(memberId) }
        emitter.onError { removeEmitter(memberId) }
        return emitter
    }

    fun add(memberId: Long): SseEmitter {
        val newEmitter = createEmitter(memberId)
        emitters[memberId] = newEmitter
        log.info("New emitter added for memberId: $memberId")
        return newEmitter
    }

    private fun removeEmitter(memberId: Long) {
        emitters.remove(memberId)
        log.info("Emitter removed for memberId: $memberId")
    }

    fun sendToMember(memberId: Long, alarmTitle: String, alarmContent: String) {
        val message = AlarmMessage(alarmTitle, alarmContent)
        sendMessageToEmitter(memberId, message)
    }

    fun sendToAll(alarmTitle: String, alarmContent: String) {
        val message = AlarmMessage(alarmTitle, alarmContent)
        emitters.forEach { (memberId, _) -> sendMessageToEmitter(memberId, message) }
    }

    private fun sendMessageToEmitter(memberId: Long, message: AlarmMessage) {
        val jsonMessage = objectMapper.writeValueAsString(message)
        emitters[memberId]?.let { emitter ->
            try {
                emitter.send(SseEmitter.event().name("alarm").data(jsonMessage))
                log.info("Sent alarm to memberId: $memberId, message: $jsonMessage")
            } catch (e: Exception) {
                log.error("Failed to send message to memberId: $memberId", e)
                removeEmitter(memberId) // 실패한 연결 제거
            }
        }
    }

    fun count(): Int = emitters.size

    data class AlarmMessage(
        val title: String,
        val content: String
    )
}
