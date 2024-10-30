package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.dto

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.InquiryStatus
import edu.example.learner_kotlin.member.entity.Member
import java.time.LocalDateTime

data class CourseInquiryDTO(
    var inquiryId: Long? = null,
    var courseId: Long? = null,
    var memberId: Long? = null,
    var inquiryTitle: String? = null,
    var inquiryContent: String? = null,
    var createdDate: LocalDateTime? = null,
    var updateDate: LocalDateTime? = null,
    var inquiryStatus: InquiryStatus? = null,
    var memberNickname: String? = null,    // 작성자 닉네임
    var profileImage: ByteArray? = null,   // 작성자 프로필 이미지
) {
    // Entity 변환 메서드
    fun toEntity() = CourseInquiry(
        course = courseId?.let { Course(it) },
        member = memberId?.let { Member(it) },
        inquiryTitle = inquiryTitle,
        inquiryContent = inquiryContent,
        inquiryStatus = inquiryStatus
    )

    // CourseInquiry 엔티티를 DTO로 변환하는 보조 생성자
    constructor(ci: CourseInquiry) : this(
        ci.inquiryId,
        ci.course?.courseId,
        ci.member?.memberId,
        ci.inquiryTitle,
        ci.inquiryContent,
        ci.createdDate,
        ci.updateTime,
        ci.inquiryStatus,
        ci.member?.nickname,
        ci.member?.profileImage,
    )
}
