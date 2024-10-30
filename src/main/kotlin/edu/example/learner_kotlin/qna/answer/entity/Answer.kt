package edu.example.learner_kotlin.qna.answer.entity

import edu.example.learner_kotlin.qna.inquiry.entity.Inquiry
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "answer")
@EntityListeners(AuditingEntityListener::class)
data class Answer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var answerId: Long? = null,

    var answerContent: String? = null,

    @CreatedDate
    var answerCreateDate: LocalDateTime? = null,

    @OneToOne @JoinColumn(name = "inquiry_id", nullable = false)
    var inquiry: Inquiry? = null,
) {
}