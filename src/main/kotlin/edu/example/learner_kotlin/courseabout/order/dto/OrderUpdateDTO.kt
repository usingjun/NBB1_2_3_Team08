package edu.leranermig.order.dto



data class OrderUpdateDTO(
    var orderId: Long? = null,
    var orderStatus: String? = "",
    var orderItemDTOList: MutableList<OrderItemDTO> = ArrayList(), // 선택적, 필요 시 업데이트 가능
    var totalPrice: Long = 0L // 추가된 필드
) {
    // 총 금액 설정


    // OrderUpdateDTO를 Order 엔티티로 변환하는 메서드
    //    public Order toEntity() {
    //        return Order.builder()
    //                .orderId(this.orderId)
    //                .orderStatus(orderStatus != null ? OrderStatus.valueOf(orderStatus) : null)
    //                .totalPrice(this.totalPrice) // 총 금액 추가
    //                .build();
    //    }
}
