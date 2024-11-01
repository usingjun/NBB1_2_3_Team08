package edu.example.learner_kotlin.courseabout.order.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.order.entity.Order
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Embeddable
@Table(name = "order_Item")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
@EntityListeners(AuditingEntityListener::class)
class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderItemId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    var course: Course? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false, updatable = false) // 변경 불가 설정
    var order: Order? = null,

    var courseAttribute: CourseAttribute? = null,

    @CreatedDate
    val createdAt: LocalDateTime? = null,

    @LastModifiedDate
    val updatedAt: LocalDateTime? = null,

    @Column(nullable = false)
    var price: Long? = null,
) {


}
