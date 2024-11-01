package edu.example.learner_kotlin.member.dto

import edu.example.learner_kotlin.member.entity.FollowStatus
import edu.example.learner_kotlin.member.entity.Member
import org.apache.tomcat.util.codec.binary.Base64

data class FollowDTO(
    var memberId: Long? = null,
    var nickname: String? = null,
    var profileImage: String? = null, // Base64 인코딩된 이미지 데이터로 변경
    var imageType: String? = null,
    var followStatus: FollowStatus? = null,
) {
    constructor(member: Member) : this(
        member.memberId,
        member.nickname,
        profileImage =
        if (member.profileImage != null) Base64.encodeBase64String(member.profileImage) else null,
        member.imageType
    )

    constructor(member: Member, followStatus: FollowStatus?) : this(
        member.memberId,
        member.nickname,
        profileImage =
        if (member.profileImage != null) Base64.encodeBase64String(member.profileImage) else null,
        member.imageType,
        followStatus = followStatus
    )
}
