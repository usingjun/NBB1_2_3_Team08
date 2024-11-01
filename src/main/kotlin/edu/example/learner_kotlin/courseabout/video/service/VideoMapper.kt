package edu.example.learner_kotlin.courseabout.video.service

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.video.dto.VideoCreateDTO
import edu.example.learner_kotlin.courseabout.video.dto.VideoDTO
import edu.example.learner_kotlin.courseabout.video.dto.VideoUpdateDTO
import edu.example.learner_kotlin.courseabout.video.entity.Video

object VideoMapper {
    fun toEntity(dto: VideoCreateDTO, course: Course): Video{
        return Video().apply {
            title = dto.title
            description = dto.description
            this.course = course
        }
    }
    fun toUpdateEntity(dto: VideoUpdateDTO, existingVideo: Video) : Video{
        return existingVideo.apply {
            title = dto.title
            description = dto.description
            url = dto.url
        }
    }
    fun toDTO(video: Video) :VideoDTO{
        return VideoDTO(
            videoId = video.video_Id,
            title = video.title,
            url = video.url,
            description = video.description,
            courseId = video.course?.courseId
        )
    }
}