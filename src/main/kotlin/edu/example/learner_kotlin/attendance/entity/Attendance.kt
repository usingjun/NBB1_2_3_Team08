package edu.example.learner_kotlin.attendance.entity

import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate

@Entity
@Table(name = "attendance")
@EntityListeners(AuditingEntityListener::class)
data class Attendance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var attendanceId: Long? = null,

    @CreatedDate
    var attendanceDate: LocalDate? = null,

    var continuous: Int? = 1,

    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member? = null
) {
}