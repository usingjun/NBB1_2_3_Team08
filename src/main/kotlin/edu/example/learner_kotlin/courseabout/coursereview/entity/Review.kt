package edu.example.learner.courseabout.coursereview.entity

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "review")
@EntityListeners(
    AuditingEntityListener::class
)
data class Review (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    var reviewId: Long? = null,

    @Column(nullable = false, unique = true)
    var reviewName: String? = null,

    @Column(nullable = false)
    var reviewDetail: String? = null,

    @Column(nullable = false, unique = true)
    var rating: Int = 0,

    @CreatedDate
    val reviewCreatedDate: LocalDateTime? = null,

    @LastModifiedDate
    val reviewUpdatedDate: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val reviewType: ReviewType? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    val course: Course? = null
){

}
