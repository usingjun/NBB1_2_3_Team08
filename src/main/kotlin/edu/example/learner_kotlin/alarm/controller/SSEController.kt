package edu.example.learner_kotlin.alarm.controller

import edu.example.learner_kotlin.alarm.entity.NotificationRequest
import edu.example.learner_kotlin.alarm.service.SseService
import edu.example.learner_kotlin.log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = ["http://localhost:3000"], allowCredentials = "true")
class SseController(private val sseService: SseService) {

    @GetMapping("/connect")
    fun connect(@RequestParam memberId: Long): SseEmitter {
        return try {
            val emitter = sseService.add(memberId)
            // 연결 확인용 이벤트를 보내어 클라이언트가 연결된 것을 확인
            emitter.send(SseEmitter.event().name("connect").data("Connected successfully"))
            emitter
        } catch (e: IOException) {
            log.error("Failed to establish connection for memberId: $memberId", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Connection failed")
        }
    }

    @PostMapping("/notify")
    fun notify(@RequestBody @Validated request: NotificationRequest): ResponseEntity<Any> {
        log.info("Received notification request: memberId={}, title={}, message={}", request.memberId, request.title, request.message)

        return try {
            if (request.memberId != null) {
                sseService.sendToMember(request.memberId, request.title, request.message)
                log.info("Notification sent to memberId: {}", request.memberId)
            } else {
                sseService.sendToAll(request.title, request.message)
                log.info("Notification sent to all members")
            }
            ResponseEntity.ok(mapOf("message" to "Notification sent successfully"))
        } catch (e: IOException) {
            log.error("Failed to send notification for request: {}", request, e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Notification delivery failed"))
        } catch (e: Exception) {
            log.error("Unexpected error occurred while sending notification: {}", e.message, e)
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/count")
    fun count(): ResponseEntity<Map<String, Int>> {
        val count = sseService.count()
        return ResponseEntity.ok(mapOf("count" to count))
    }
}
