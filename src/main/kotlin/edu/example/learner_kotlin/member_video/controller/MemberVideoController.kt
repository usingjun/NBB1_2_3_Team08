package edu.example.learner_kotlin.member_video.controller

import edu.example.learner_kotlin.member_video.dto.MemberVideoDTO
import edu.example.learner_kotlin.member_video.service.MemberVideoService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member-video")
class MemberVideoController(val memberVideoService: MemberVideoService) {
    @GetMapping("/{memberId}/{videoId}")
    fun readByMemberIdAndVideoId(@PathVariable memberId: Long, @PathVariable videoId: Long) = run {
        ResponseEntity.ok(memberVideoService.isPresentByMemberIdAndVideoId(memberId, videoId))
    }

    @PostMapping
    fun create(@Validated @RequestBody memberVideoDTO: MemberVideoDTO) = run {
        ResponseEntity.ok(memberVideoService.create(memberVideoDTO))
    }

    @PutMapping("/{memberId}/{videoId}")
    fun update(
        @PathVariable memberId: Long,
        @PathVariable videoId: Long,
        @Validated @RequestBody memberVideoDTO: MemberVideoDTO
    ) = run {
        ResponseEntity.ok(memberVideoService.update(memberVideoDTO))
    }

    @GetMapping("/{videoId}/average")
    fun getAverage(@PathVariable videoId: Long) = run {
        ResponseEntity.ok(memberVideoService.getAverageByVideoId(videoId))
    }

    @GetMapping("/{memberId}/{videoId}/watched")
    fun getWatched(@PathVariable memberId: Long, @PathVariable videoId: Long) = run {
        ResponseEntity.ok(memberVideoService.isWatchedByMemberIdAndVideoId(memberId, videoId))
    }
}