package edu.example.learner.courseabout.video.controller

import edu.example.learner_kotlin.courseabout.video.dto.WatchTimeDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class WatchTimeController {
    @PostMapping("/test/watch")
    fun receiveWatchTime(@RequestBody watchTimeDTO: WatchTimeDTO): ResponseEntity<Void> {
        // 시청 시간 처리 로직 (DB 저장 등)
        println("Current Time: " + watchTimeDTO.currentTime)
        println("Duration: " + watchTimeDTO.duration)

        // 응답 반환
        return ResponseEntity.ok().build()
    }
}