//package edu.example.learner_kotlin.alarm.controller
//
//import edu.example.learner_kotlin.log
//import org.springframework.stereotype.Component
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
//import java.util.concurrent.ConcurrentHashMap
//
//@Component
//class SseEmitters {
//    private val emitters = ConcurrentHashMap<Long, SseEmitter>()
//
//    fun add(memberId: Long, emitter: SseEmitter) {
//        emitters[memberId] = emitter
//        emitter.onCompletion { emitters.remove(memberId) }
//        emitter.onTimeout { emitters.remove(memberId) }
//    }
//
//    fun sendToMember(memberId: Long, message: String) {
//        val emitter = emitters[memberId]
//        emitter?.let {
//            try {
//                it.send(SseEmitter.event()
//                    .name("message")
//                    .data(message))
//            } catch (e: Exception) {
//                emitters.remove(memberId)
//                log.error("Failed to send message to member $memberId", e)
//            }
//        }
//    }
//
//    fun sendToAll(message: String) {
//        val deadEmitters = mutableListOf<Long>()
//        emitters.forEach { (memberId, emitter) ->
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("message")
//                    .data(message))
//            } catch (e: Exception) {
//                deadEmitters.add(memberId)
//            }
//        }
//        deadEmitters.forEach { emitters.remove(it) }
//    }
//
//    fun count(): Int {
//        return emitters.size
//    }
//}