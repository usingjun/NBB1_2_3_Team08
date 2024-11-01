package edu.example.learner_kotlin.member.dto

import edu.example.learner_kotlin.member.entity.Member
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class MemberDTO(
    var memberId: Long? = null,                  // 멤버 ID
    @field:NotNull var email: String?,           // 이메일 (필수)
    var password: String?,                       // 비밀번호 (필수)
    @field:NotNull var nickname: String?,        // 닉네임 (필수)
    var phoneNumber: String? = null,             // 전화번호 (선택적)
    var profileImage: ByteArray? = null,         // 프로필 이미지 (선택적)
    var imageType: String? = null,               // 이미지 타입 (선택적)
    var introduction: String? = null,            // 자기소개 (선택적)
    var createDate: LocalDateTime? = null        // 생성일 (선택적)
){
    constructor(member : Member) : this(
        member.memberId,
        member.email,
        member.password,
        member.nickname,
        member.phoneNumber,
        member.profileImage,
        member.imageType,
        member.introduction,
        member.createDate
    )

    fun otherMember(): MemberDTO {
        this.password = null
        this.createDate = null

        return this
    }
}
