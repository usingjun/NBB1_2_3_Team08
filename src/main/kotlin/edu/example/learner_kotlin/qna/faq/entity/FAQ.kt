package edu.example.learner_kotlin.qna.faq.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "faq")
@EntityListeners(AuditingEntityListener::class)
data class FAQ(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val faqId: Long? = null,

    var faqTitle: String? = null,

    var faqContent: String? = null,

    @CreatedDate
    var faqCreateDate: LocalDateTime? = null,

    @LastModifiedDate
    var faqUpdateDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    var faqCategory: FAQCategory? = null,
) {
}