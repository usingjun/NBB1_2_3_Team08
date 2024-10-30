package edu.example.learner_kotlin.courseabout.video.dto

import edu.example.learner_kotlin.courseabout.video.entity.Video
import java.time.LocalDateTime


class VideoDTO(
    var videoId: Long? = null,
    var courseId: Long? = null,
    var title: String? = null,
    var url: String? = null,
    var description: String? = null,
    var totalVideoDuration: Long? = null,
    var currentVideoTime: Long? = null,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null
) {
    constructor(video: Video) : this(
        videoId = video.video_Id,
        courseId = video.course?.courseId,
        title = video.title,
        url = video.url,
        description = video.description,
        totalVideoDuration = video.totalVideoDuration,
        currentVideoTime = video.currentVideoTime,
        createdAt = video.createdAt,
        updatedAt = video.updatedAt
    )
}
