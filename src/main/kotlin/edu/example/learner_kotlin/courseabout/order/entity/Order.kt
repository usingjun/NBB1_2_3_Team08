package edu.example.learner_kotlin.courseabout.order.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import edu.example.learner_kotlin.member.entity.Member
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Embeddable
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener::class)
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" ,nullable = false)
    var member: Member? = null,

    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var orderStatus: OrderStatus = OrderStatus.PENDING, // 기본값 설정

    var totalPrice: Long = 0, // 기본값 설정

)
{
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val orderItems: MutableList<OrderItem> = ArrayList()
}
