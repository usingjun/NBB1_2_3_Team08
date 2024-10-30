package edu.example.learner_kotlin.courseabout.order.service

import edu.leranermig.order.dto.OrderDTO
import edu.leranermig.order.dto.OrderUpdateDTO


interface OrderService {
    fun add(orderDTO: OrderDTO, memberId: Long): OrderDTO
    fun read(orderId: Long): OrderDTO?
    fun update(orderUpdateDTODTO: OrderUpdateDTO, orderId: Long): OrderUpdateDTO

    fun delete(courseId: Long)
    fun readAll(): List<OrderDTO>
    fun getOrdersById(memberId: Long): List<OrderDTO>
    fun deleteAll()
    fun purchaseOrderItems(orderId: Long, memberId: Long): OrderDTO;
}
