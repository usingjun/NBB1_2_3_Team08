package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "course_answer")
@EntityListeners(AuditingEntityListener::class)
data class CourseAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var answerId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null,

    var answerContent: String? = null,

    @CreatedDate
    var answerCreateDate: LocalDateTime? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "inquiry_id")
    @JsonBackReference
    var courseInquiry: CourseInquiry? = null
){
    fun changeAnswerContent(answerContent: String) {
        this.answerContent = answerContent
    }
}
