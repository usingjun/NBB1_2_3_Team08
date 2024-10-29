package edu.example.learner_kotlin.member.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
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
    private var memberId: Long? = null,

    @Column(nullable = false, unique = true)
    private var email: String? = null,

    @Column(nullable = true)
    private var password: String? = null,

    @Column(nullable = false, unique = true)
    private var nickname: String? = null,

    @Column(nullable = true)
    private var phoneNumber: String? = null,

    @Lob // BLOB 타입으로 처리됨
    private var profileImage: ByteArray,

    private var imageType: String? = null
    ,
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private var role: Role? = null,

    @Column(nullable = true)
    private var introduction: String? = null,

    @CreatedDate
    private val createDate: LocalDateTime? = null
){
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val heartNewsList: List<HeartNews> = ArrayList<HeartNews>()
//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val memberCourses: List<MemberCourse> = ArrayList<MemberCourse>()
//
//    @OneToMany(mappedBy = "member", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    private val courses: List<Course> = ArrayList<Course>()
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