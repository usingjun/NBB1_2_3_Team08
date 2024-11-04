package edu.example.learner_kotlin.alarm.service

import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Service
class SseService {
    private val emitters = ConcurrentHashMap<Long, SseEmitter>()

    fun add(memberId: Long, emitter: SseEmitter) {
        emitters[memberId] = emitter
        emitter.onCompletion { emitters.remove(memberId) }
        emitter.onTimeout { emitters.remove(memberId) }
    }

    fun sendToMember(memberId: Long, message: String) {
        val jsonMessage = "{\"message\":\"$message\"}" // JSON 형식으로 변환
        emitters[memberId]?.let { emitter ->
            try {
                emitter.send(SseEmitter.event().name("message").data(jsonMessage))
            } catch (e: Exception) {
                emitters.remove(memberId)
            }
        }
    }


    fun sendToAll(message: String) {
        emitters.forEach { (_, emitter) ->
            try {
                emitter.send(SseEmitter.event().name("message").data(message))
            } catch (e: Exception) {
                // 실패한 경우 제거
            }
        }
    }

    fun count(): Int {
        return emitters.size
    }
}
