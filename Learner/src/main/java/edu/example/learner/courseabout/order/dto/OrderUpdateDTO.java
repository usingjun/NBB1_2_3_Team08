package edu.example.learner.courseabout.order.dto;

import edu.example.learner.courseabout.order.entity.Order;
import edu.example.learner.courseabout.order.entity.OrderStatus;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderUpdateDTO {
    private Long orderId; // 필수 필드
    private String orderStatus; // 수정할 수 있는 필드
    private List<OrderItemDTO> orderItemDTOList; // 선택적, 필요 시 업데이트 가능
    private Double totalPrice; // 추가된 필드

    public OrderUpdateDTO(Order order) {
        this.orderId = order.getOrderId();
        this.orderStatus = order.getOrderStatus().toString();
        this.totalPrice = order.getTotalPrice(); // 총 금액 설정
        this.orderItemDTOList = new ArrayList<>();
        order.getOrderItems().forEach(item -> {
            this.orderItemDTOList.add(new OrderItemDTO(item));
        });
    }

    // OrderUpdateDTO를 Order 엔티티로 변환하는 메서드
    public Order toEntity() {
        return Order.builder()
                .orderId(this.orderId)
                .orderStatus(orderStatus != null ? OrderStatus.valueOf(orderStatus) : null)
                .totalPrice(this.totalPrice) // 총 금액 추가
                .build();
    }
}
