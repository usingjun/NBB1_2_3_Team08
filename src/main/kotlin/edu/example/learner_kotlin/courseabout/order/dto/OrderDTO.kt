package edu.example.learner_kotlin.courseabout.order.dto

import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.course.entity.CourseAttribute
import edu.example.learner_kotlin.courseabout.order.entity.Order
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem
import edu.example.learner_kotlin.courseabout.order.entity.OrderStatus
import edu.example.learner_kotlin.member.entity.Member
import edu.leranermig.order.dto.OrderItemDTO


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
            orderItems=listToEntity(orderItemDTOList!!)
        }
        return order
    }
    private fun listToEntity(orderItemDTOList: MutableList<OrderItemDTO>): MutableList<OrderItem> {
        var orderItemList = mutableListOf<OrderItem>()
        for (orderItemDTO in orderItemDTOList) {
            orderItemList.add( OrderItem().apply {
                course = Course(courseId = orderItemDTO.courseId)
                courseAttribute= CourseAttribute.fromString(orderItemDTO.courseAttribute!!)
                price = orderItemDTO.price
            }) }
        return orderItemList
        }
    constructor(order: Order) : this(
        orderId = order.orderId,
        memberId = order.member!!.memberId,
        orderStatus = order.orderStatus.toString(),
        totalPrice = order.totalPrice,
        createdDate = order.createdAt.toString()
    )
}
