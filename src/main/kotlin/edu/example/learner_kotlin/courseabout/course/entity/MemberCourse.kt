package edu.example.learner_kotlin.courseabout.course.entity

import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*


@Entity
@Table(name = "member_course")
@EntityListeners(AuditingEntityListener::class)
data class MemberCourse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_course_id")
    var memberCourse: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "member_id")
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", referencedColumnName = "course_id")
    var course: Course? = null,

    @CreatedDate
    @Column(name = "purchase_date")
    var purchaseDate: Date? = null,

){

}
