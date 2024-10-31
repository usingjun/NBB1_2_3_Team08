package edu.example.learner_kotlin.studytable.entity

import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate

@Entity
@Table(name = "study_table")
@EntityListeners(AuditingEntityListener::class)
data class StudyTable(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var studyTableId: Long? = null,

    @CreatedDate
    var studyDate: LocalDate? = null,

    var studyTime: Int = 0,

    var completed: Int = 0,

    @ManyToOne @JoinColumn(name = "member_id")
    var member: Member? = null,
) {
}