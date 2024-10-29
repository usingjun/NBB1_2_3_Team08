package edu.example.learner.courseabout.order.repository;

import edu.example.learner.courseabout.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
