package edu.example.learner_kotlin.courseabout.video.controller


import edu.example.learner_kotlin.courseabout.video.dto.VideoCreateDTO
import edu.example.learner_kotlin.courseabout.video.dto.VideoDTO
import edu.example.learner_kotlin.courseabout.video.dto.VideoUpdateDTO
import edu.example.learner_kotlin.courseabout.video.service.VideoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Parameter

@RestController
@RequestMapping("/video")
@Tag(name = "비디오 컨트롤러", description = "비디오 CRUD를 담당하는 컨트롤러입니다.")
class VideoController (
    private val videoService: VideoService,
) {
    private val totalDuration = 0.0 // 전체 동영상 시간
    private val currentTime = 0.0 // 현재 재생 시간

    @PostMapping("/savePlayTime")
    @Operation(summary = "재생 시간 저장", description = "비디오 ID와 재생 시간을 저장합니다.")
    fun savePlayTime(@Parameter(description = "재생 시간 요청 데이터") @RequestBody playTimeRequest: PlayTimeRequest): String {
        val videoId: Long = playTimeRequest.videoId!!

        val videoById = videoService.getVideoById(videoId)

        if (playTimeRequest.totalDuration > 0.0) {
            videoById.totalVideoDuration = playTimeRequest.currentTime.toLong() // 전체 동영상 시간 업데이트
            println("전체 동영상 시간: " + videoById.totalVideoDuration + "초")
        } else {
            videoById.currentVideoTime = playTimeRequest.currentTime.toLong() // 현재 재생 시간 업데이트
            println("현재 동영상 재생 시간: " + videoById.currentVideoTime + "초")
        }
        return "동영상 시간 전달 완료"
    }

    @Operation(summary = "비디오 정보 조회", description = "전체 동영상 시간과 현재 재생 시간을 조회합니다.")
    @GetMapping("/videoInfo")
    fun videoInfo() = run {
        val videoInfo = VideoInfo(totalDuration, currentTime)
        ResponseEntity.ok(videoInfo)
    }

    @Operation(summary = "모든 비디오 조회", description = "관리자가 모든 비디오 목록을 조회합니다.")
    @GetMapping("/list/admin")
    fun allVideos() = run {
        videoService.allVideos
    }

    @GetMapping("/{id}")
    @Operation(summary = "비디오 조회", description = "특정 ID의 비디오를 조회합니다.")
    fun getVideoById(@Parameter(description = "조회할 비디오 ID") @PathVariable id: Long): ResponseEntity<VideoDTO> =
        ResponseEntity.ok(VideoDTO(videoService.getVideoById(id)))

    @PostMapping
    @Operation(summary = "비디오 추가", description = "새로운 비디오를 추가합니다.")
    fun addVideo(@Parameter(description = "추가할 비디오 데이터") @RequestBody dto: VideoCreateDTO): ResponseEntity<VideoDTO> {
        val createdVideo = videoService.addVideo(dto)
        return ResponseEntity.status(201).body(createdVideo)
    }

    @PutMapping("/{id}")
    @Operation(summary = "비디오 수정", description = "특정 ID의 비디오 정보를 수정합니다.")
    fun updateVideo(
        @Parameter(description = "수정할 비디오 ID") @PathVariable id: Long,
        @Parameter(description = "수정할 비디오 데이터") @RequestBody dto: VideoUpdateDTO
    )=ResponseEntity.ok(videoService.updateVideo(id, dto))


    @DeleteMapping("/{id}")
    @Operation(summary = "비디오 삭제", description = "특정 ID의 비디오를 삭제합니다.")
    fun deleteVideo(@Parameter(description = "삭제할 비디오 ID") @PathVariable id: Long) = run {
        videoService.deleteVideo(id)
        ResponseEntity.ok().body(mapOf("success" to "delete"))
    }


    class PlayTimeRequest {
        val totalDuration = 0.0
        val currentTime = 0.0
        val videoId: Long? = null
    }

    class VideoInfo(val totalDuration: Double, val currentTime: Double) {
    }
}
