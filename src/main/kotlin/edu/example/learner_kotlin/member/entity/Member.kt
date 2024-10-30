package edu.example.learner_kotlin.member.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.MemberCourse
import jakarta.persistence.*
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
    var profileImage: ByteArray? =null,

    var imageType: String? = null,
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var role: Role? = null,

    @Column(nullable = true)
    var introduction: String? = null,

    @CreatedDate
    val createDate: LocalDateTime? = null,

){
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val heartNewsList: List<HeartNews> = ArrayList<HeartNews>()
//
    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    val memberCourses: List<MemberCourse> = ArrayList<MemberCourse>()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    private val courses: List<Course> = ArrayList<Course>()
//

//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val inquiries: List<Inquiry> = ArrayList<Inquiry>()
//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val courseInquiries: List<CourseInquiry> = ArrayList<CourseInquiry>()
//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val courseAnswers: List<CourseAnswer> = ArrayList<CourseAnswer>()
//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val reviews: List<Review> = ArrayList<Review>()
}