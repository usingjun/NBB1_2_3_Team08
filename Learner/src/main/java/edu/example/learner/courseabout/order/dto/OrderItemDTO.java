package edu.example.learner.courseabout.order.dto;

import edu.example.learner.courseabout.course.entity.Course;
import edu.example.learner.courseabout.course.entity.CourseAttribute;
import edu.example.learner.courseabout.order.entity.Order;
import edu.example.learner.courseabout.order.entity.OrderItem;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Data
public class OrderItemDTO {

    private Long orderId;
    private Long courseId;

    private String courseName;
    private String courseAttribute;
    private Long price;





    public OrderItemDTO(OrderItem orderItem) {
        this.courseId = orderItem.getCourse().getCourseId();
        this.courseAttribute = String.valueOf(orderItem.getCourse().getCourseAttribute());

        this.price = orderItem.getCourse().getCoursePrice();
        this.orderId = orderItem.getOrder().getOrderId();
        this.courseName = orderItem.getCourse().getCourseName();
    }

    public OrderItem toEntity(OrderItemDTO orderItemDTO, Order order) {
        return OrderItem.builder()
                .order(order) // 외부에서 전달된 Order를 사용
                .course(Course.builder()
                        .courseId(orderItemDTO.getCourseId()).coursePrice(orderItemDTO.price).build())
                .price(orderItemDTO.getPrice())
                .courseAttribute(CourseAttribute.valueOf(orderItemDTO.getCourseAttribute()))
                .build();
    }
  
}
