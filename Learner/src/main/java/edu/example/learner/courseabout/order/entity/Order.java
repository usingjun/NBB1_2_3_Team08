package edu.example.learner.courseabout.order.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.example.learner.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Embeddable
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id" ,nullable = false)
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Double totalPrice;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void changeOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null) {
            orderItems.add(orderItem);
            orderItem.setOrder(this); // OrderItem에 현재 Order 설정
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItems.remove(orderItem)) {
            orderItem.setOrder(null); // OrderItem의 order 필드 null 설정
        }
    }
    public void changeTotalPrice(Double price) {
        this.totalPrice = price;
    }
}
