package edu.example.learner.member.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import edu.example.learner.courseabout.course.entity.Course
import edu.example.learner.courseabout.course.entity.MemberCourse
import edu.example.learner.courseabout.courseqna.entity.CourseAnswer
import edu.example.learner.courseabout.courseqna.entity.CourseInquiry
import edu.example.learner.courseabout.coursereview.entity.Review
import edu.example.learner.courseabout.news.entity.HeartNews
import edu.example.learner.qna.inquiry.entity.Inquiry
import jakarta.persistence.*
import lombok.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "member", indexes = [Index(columnList = "email")])
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties("hibernateLazyInitializer", "handler") // Hibernate 프록시 필드를 무시
data class Member (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var memberId: Long? = null,

    @Column(nullable = false, unique = true)
    var email: String? = null,

    @Column(nullable = true)
    var password: String? = null,

    @Column(nullable = false, unique = true)
    var nickname: String? = null,

    @Column(nullable = true)
    var phoneNumber: String? = null,

    @Lob // BLOB 타입으로 처리됨
    var profileImage: ByteArray? = null,

    var imageType: String? = null,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var role: Role? = null,

    @Column(nullable = true)
    var introduction: String? = null,

    @CreatedDate
    val createDate: LocalDateTime? = null
    ){
    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val heartNewsList: List<HeartNews> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val memberCourses: List<MemberCourse> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val courses: List<Course> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val inquiries: List<Inquiry> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val courseInquiries: List<CourseInquiry> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val courseAnswers: List<CourseAnswer> = ArrayList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val reviews: List<Review> = ArrayList()
}
