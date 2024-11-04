package edu.example.learner_kotlin.courseabout.course.entity

//import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.entity.CourseInquiry
import edu.example.learner_kotlin.courseabout.coursereview.entity.Review
import edu.example.learner_kotlin.courseabout.news.entity.NewsEntity
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.video.entity.Video
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Entity
@Table(name = "course")
@EntityListeners(AuditingEntityListener::class)
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    var courseId: Long? = null,
    var courseName: @NotNull String? = null,
    var courseDescription: @NotEmpty String? = null,

    @Enumerated(EnumType.STRING)
    var courseAttribute: CourseAttribute? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname", referencedColumnName = "nickname")
    var member: Member?= null,

    var coursePrice: @NotNull Long? = null,
    var courseLevel: @NotNull @Max(5) @Min(1) Int? = null,
//    @OneToMany(mappedBy = "course", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
//    val memberCourses: List<MemberCourse> = emptyList(),
    var sale : Boolean = false,
    @CreatedDate
    var courseCreatedDate: LocalDateTime? = null,

    @LastModifiedDate
    var courseModifiedDate: LocalDateTime? = null,
    )
{

    @OneToMany(mappedBy = "courseNews", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var newsEntities: MutableList<NewsEntity> = mutableListOf()

    @OneToMany(mappedBy = "course", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    val memberCourses: List<MemberCourse> = emptyList()
    //    @OneToMany(mappedBy = "courseNews", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    //    private List<NewsEntity> newsEntities = new ArrayList<>();
    //
    @OneToMany(mappedBy = "course",cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var  orderItems : MutableList<OrderItem> = mutableListOf()

    @OneToMany(mappedBy = "course", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var videos : MutableList<Video> = mutableListOf()

    @OneToMany(mappedBy = "course", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var reviews : MutableList<Review> = mutableListOf()

    @OneToMany(mappedBy = "course",cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var courseInquiries : MutableList<CourseInquiry> = mutableListOf()


}
