package edu.leranermig.order.dto

import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.example.learner_kotlin.member.entity.Member


data class OrderDTO(
    var orderId : Long? = null,
    var memberId: Long? = null,
    var orderItemDTOList: MutableList<OrderItemDTO>? = null,
    var orderStatus: String = "",
    var createdDate: String = "",
    var totalPrice: Long = 0L
) {
    fun toEntity(orderDTO: OrderDTO): Order{
        var order = Order().apply {
            orderStatus = OrderStatus.valueOf(orderDTO.orderStatus)
            member = Member(memberId = orderDTO.memberId)
            totalPrice= orderDTO.totalPrice
        }
        return order
    }
    constructor(order: Order) : this(
        orderId = order.orderId,
        memberId = order.member!!.memberId,
        orderStatus = order.orderStatus.toString(),
        totalPrice = order.totalPrice
    )
//    fun toEntity(orderDTO: OrderDTO): Order {
//        return Order.builder()
//            .orderId(orderDTO.getOrderId())
//            .orderStatus(OrderStatus.ACCEPT)
//            .member(Member.builder().memberId(memberId).build())
//            .build()
//    }
}
