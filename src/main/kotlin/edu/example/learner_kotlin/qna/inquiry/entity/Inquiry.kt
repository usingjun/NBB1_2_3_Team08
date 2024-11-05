package edu.example.learner_kotlin.qna.inquiry.entity

import edu.example.learner_kotlin.member.entity.Member
import edu.example.learner_kotlin.qna.answer.entity.Answer
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "inquiry")
@EntityListeners(AuditingEntityListener::class)
data class Inquiry(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var inquiryId: Long? = null,

    var inquiryTitle: String? = null,

    var inquiryContent: String? = null,

    @CreatedDate
    var inquiryCreateDate: LocalDateTime? = null,

    @LastModifiedDate
    var inquiryUpdateDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var inquiryStatus: InquiryStatus? = null,

    @ManyToOne @JoinColumn(name = "member_id")
    var member: Member? = null,
) {
    @OneToOne(mappedBy = "inquiry", cascade = [CascadeType.ALL], orphanRemoval = true)
    var answer: Answer? = null
}