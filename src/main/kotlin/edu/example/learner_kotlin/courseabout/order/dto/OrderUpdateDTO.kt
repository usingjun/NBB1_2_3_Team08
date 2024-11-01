package edu.example.learner_kotlin.courseabout.order.dto

import edu.leranermig.order.dto.OrderItemDTO

data class OrderUpdateDTO (
    val orderId : Long,
    var orderStatus: String,
    var orderItemDTOList: MutableList<OrderItemDTO> = mutableListOf(),
    var totalPrice : Long
) {

}