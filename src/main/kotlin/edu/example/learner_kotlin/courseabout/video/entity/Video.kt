package edu.example.learner_kotlin.courseabout.video.entity

import edu.example.learner_kotlin.courseabout.course.entity.Course
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "videos")
@EntityListeners(
    AuditingEntityListener::class
)
data class Video (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val video_Id: Long? = null,

    var title: String? = null,

    var url: String? = null,

    var description: String? = null,

    var totalVideoDuration: Long? = null, // 전체 동영상 시간 추가
    var currentVideoTime: Long? = null, // 현재 동영상 시간 추가

    @CreatedDate
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    val updatedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_Id", referencedColumnName = "course_id") // course_id에 맞추기
    var course: Course? = null,
){
    // 초기 동영상 시간 설정 메서드
    fun initializeTimes(totalDuration: Long?, currentTime: Long?) {
        this.totalVideoDuration = totalDuration
        this.currentVideoTime = currentTime
    }
}
