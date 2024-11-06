package edu.example.learner_kotlin.member_video.entity

import edu.example.learner_kotlin.courseabout.video.entity.Video
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "member_video")
data class MemberVideo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var watched: Boolean? = false,

    var studyTime: Long? = 0L,
) {
    @ManyToOne @JoinColumn(name = "member_id")
    var member: Member? = null

    @ManyToOne @JoinColumn(name = "video_id")
    var video: Video? = null
}