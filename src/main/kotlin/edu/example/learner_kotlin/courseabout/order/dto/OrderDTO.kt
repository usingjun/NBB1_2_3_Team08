package edu.leranermig.order.dto


data class OrderDTO(
    var orderId : Long? = null,
    var memberId: Long? = null,
    var orderItemDTOList: MutableList<OrderItemDTO>? = null,
    var orderStatus: String = "",
    val createdDate: String = "",
    var totalPrice: Long = 0L
) {
//    fun toEntity(orderDTO: OrderDTO): Order {
//        return Order.builder()
//            .orderId(orderDTO.getOrderId())
//            .orderStatus(OrderStatus.ACCEPT)
//            .member(Member.builder().memberId(memberId).build())
//            .build()
//    }
}
