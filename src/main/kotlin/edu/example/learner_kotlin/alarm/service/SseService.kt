package edu.example.learner_kotlin.alarm.service

import edu.example.learner_kotlin.log
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

    fun sendToMember(memberId: Long, alarmTitle: String, alarmContent: String) {
        val message = mapOf(
            "title" to alarmTitle,
            "content" to alarmContent
        )

        emitters[memberId]?.let { emitter ->
            try {
                emitter.send(SseEmitter.event().name("alarm").data(message))
                log.info("알람 보내기 $message")
            } catch (e: Exception) {
                emitters.remove(memberId)
            }
        }
        
    }

    fun sendToAll(alarmTitle: String, alarmContent: String) {
        val message = mapOf(
            "title" to alarmTitle,
            "content" to alarmContent
        )

        emitters.forEach { (_, emitter) ->
            try {
                emitter.send(SseEmitter.event().name("alarm").data(message))
            } catch (e: Exception) {
                // 실패한 경우 제거
            }
        }
    }

    fun count(): Int {
        return emitters.size
    }
}
