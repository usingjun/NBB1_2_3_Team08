package edu.leranermig.order.dto


import edu.example.learner_kotlin.courseabout.course.entity.Course
import edu.example.learner_kotlin.courseabout.order.entity.OrderItem


data class OrderItemDTO(
    var orderId: Long? = null,
    var courseId: Long? = null,
    var courseName: String? = null,
    var courseAttribute: String? = null,
    var price: Long? = null,

) {
    fun toEntity(orderItemDTO: OrderItemDTO): OrderItem {
        return OrderItem().apply {
            orderId = orderItemDTO.orderId
            courseId = orderItemDTO.courseId
            course = Course( courseName=courseName, courseAttribute=courseAttribute)
            price = price

        }
    }
    constructor(orderItem: OrderItem) : this(
        orderId = orderItem.orderItemId,
        courseId = orderItem.course?.courseId,
        courseName = orderItem.course?.courseName,
        courseAttribute = orderItem.courseAttribute.toString(),
        price = orderItem.price
    )
}