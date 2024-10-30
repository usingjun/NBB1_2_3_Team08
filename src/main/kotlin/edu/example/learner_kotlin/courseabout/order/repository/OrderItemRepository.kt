package edu.example.learner_kotlin.courseabout.order.repository

import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import org.springframework.data.jpa.repository.JpaRepository


interface OrderItemRepository : JpaRepository<OrderItem?, Long?>
