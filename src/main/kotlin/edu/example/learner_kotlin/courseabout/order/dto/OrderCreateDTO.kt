package edu.example.learner_kotlin.courseabout.order.dto

import edu.leranermig.order.dto.OrderItemDTO

data class OrderCreateDTO (
    var memberId : Long,
    var orderItemDTOList: MutableList<OrderItemDTO>? = mutableListOf(),
    var orderStatus: String = "",
    var createdDate : String = "",
    var totalPrice : Long = 0L,
){
}