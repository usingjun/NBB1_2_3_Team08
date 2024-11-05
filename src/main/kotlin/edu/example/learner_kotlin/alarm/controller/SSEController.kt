package edu.example.learner_kotlin.alarm.controller

import edu.example.learner_kotlin.alarm.entity.NotificationRequest
import edu.example.learner_kotlin.alarm.service.SseService
import edu.example.learner_kotlin.log
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException

@RestController
@RequestMapping("/notifications")
class SseController(private val sseService: SseService) {

    @GetMapping(value = ["/connect"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun connect(@RequestParam memberId: Long): ResponseEntity<SseEmitter> {
        val emitter = SseEmitter()
        sseService.add(memberId, emitter)
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!"))
            log.info("Received memberId: $memberId")
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return ResponseEntity.ok(emitter)
    }

    @PostMapping("/notify")
    fun notify(@RequestBody @Validated request: NotificationRequest): ResponseEntity<Any> {
        log.info("알림 메시지 수신: memberId={}, message={}", request.memberId, request.message)
        return try {
            request.memberId?.let { memberId ->
                sseService.sendToMember(memberId, request.title,request.message)
            } ?: run {
                sseService.sendToAll(request.title, request.message)
            }
            ResponseEntity.ok(mapOf("message" to "알림이 전송되었습니다"))
        } catch (e: Exception) {
            log.error("알림 전송 실패", e)
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/count")
    fun count(): ResponseEntity<Map<String, Int>> {
        val count = sseService.count()
        return ResponseEntity.ok(mapOf("count" to count))
    }
}
