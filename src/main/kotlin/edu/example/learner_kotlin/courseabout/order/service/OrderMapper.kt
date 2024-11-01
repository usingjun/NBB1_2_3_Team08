package edu.example.learner_kotlin.courseabout.order.service

import edu.example.learner_kotlin.courseabout.order.dto.OrderCreateDTO
import edu.example.learner_kotlin.courseabout.order.dto.OrderDTO
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.example.learner_kotlin.log
import edu.example.learner_kotlin.member.entity.Member


object OrderMapper{

    fun toEntity(dto: OrderCreateDTO): Order{
        return Order().apply {
            member = Member(dto.memberId)
            log.info(orderItems.toString())
            totalPrice = totalPriceCir(orderItems)
            orderStatus = OrderStatus.PENDING
        }
    }

    fun totalPriceCir(orderItems: List<OrderItem>): Long{
        var totalPrice = 0L
        orderItems.forEach {
            totalPrice+=it.price!!
        }
        return totalPrice
    }
    fun entityListToDTO(orderList: MutableList<Order?>): List<OrderDTO>{
        return orderList.map { toDTO(it!!) }
    }
    fun toDTO(order: Order): OrderDTO {
        return OrderDTO().apply {
            orderId = order.orderId
            memberId =order.member?.memberId
            orderStatus=order.orderStatus.name
            totalPrice=order.totalPrice
            createdDate=order.createdAt.toString()
            orderItemDTOList = order.orderItems.map { OrderItemMapper.toDTO(it, it.course!!) }.toMutableList()
        }
    }
}