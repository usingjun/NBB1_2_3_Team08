package edu.example.learner.courseabout.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"order"})
@Table(name = "order_Item")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId", nullable = false, updatable = false) // 변경 불가 설정
    private Order order;

    private CourseAttribute courseAttribute;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Long price;

    // OrderItem 생성 시 Order 설정
    public void setOrder(Order order) {
        if (this.order == null) { // Order가 아직 설정되지 않은 경우만
            this.order = order;
        }
    }
}
