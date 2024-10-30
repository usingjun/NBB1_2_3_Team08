package edu.example.learner_kotlin.courseabout.video.repository

import edu.example.learner_kotlin.courseabout.video.entity.Video
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<Video?, Long?> {
    fun findByCourse_CourseId(courseId: Long?): List<Video?>?
}
