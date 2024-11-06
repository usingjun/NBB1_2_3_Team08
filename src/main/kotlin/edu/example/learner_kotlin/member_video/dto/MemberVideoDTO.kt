package edu.example.learner_kotlin.member_video.dto

import edu.example.learner_kotlin.member_video.entity.MemberVideo

data class MemberVideoDTO(
    var id: Long? = null,

    var watched: Boolean? = false,

    var studyTime: Long? = 0L,

    var memberId: Long? = null,

    var videoId: Long? = null,
) {
    constructor(memberVideo: MemberVideo) : this(
        memberVideo.id,
        memberVideo.watched,
        memberVideo.studyTime,
        memberVideo.member?.memberId,
        memberVideo.video?.videoId
    )
}