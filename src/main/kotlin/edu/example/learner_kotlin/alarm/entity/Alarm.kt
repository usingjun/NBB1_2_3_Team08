package edu.example.learner_kotlin.alarm.entity

import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date


@EntityListeners(AuditingEntityListener::class)
@Table(name = "alarm")
@Entity
data class Alarm (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     val alarmId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    var alarmContent: String? = null,

    var alarmTitle: String? = null,

    @Enumerated(EnumType.STRING)
    var alarmType: AlarmType? = null,

    @Enumerated(EnumType.STRING)
    var priority: Priority? = null,

    @CreatedDate
    var createdAt: LocalDate? = null,

    var alarmStatus: Boolean = false
){


}