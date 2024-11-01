package edu.example.learner_kotlin.alarm.controller

import edu.example.learner_kotlin.log
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.CopyOnWriteArrayList

//@Component
//class SseEmitters (private val emitters: List<SseEmitter> = CopyOnWriteArrayList<SseEmitter>()) {
//    fun add(emitter: SseEmitter) {
//        this.emitters.add(emitter)
//        log.info("new emitter added: {}", emitter);
//        log.info("emitter list size: {}", emitters.size());
//    }
//}