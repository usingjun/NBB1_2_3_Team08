package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "course_inquiry")
@EntityListeners(AuditingEntityListener::class)
data class CourseInquiry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var inquiryId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = null,

    var inquiryTitle : String? = null,
    var inquiryContent: String? = null,

    @CreatedDate
    var createdDate: LocalDateTime? = null,

    @LastModifiedDate
    var updateTime: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var inquiryStatus: InquiryStatus? = null,
){
    @OneToMany(mappedBy = "courseInquiry", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    @JsonManagedReference
    var answers: MutableList<CourseAnswer> = mutableListOf()

    fun changeInquiryTitle(inquiryTitle: String){
        this.inquiryTitle = inquiryTitle
    }

    fun changeInquiryContent(inquiryContent: String){
        this.inquiryContent = inquiryContent
    }

    fun changeInquiryStatus(inquiryStatus: InquiryStatus){
        this.inquiryStatus = inquiryStatus
    }
}
