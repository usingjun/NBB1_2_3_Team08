package edu.example.learner_kotlin.courseabout.order.entity

enum class OrderStatus {
    ACCEPT,
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    REFUNDED,
    CANCELED;

    fun stringToOrderStatus(status: String): OrderStatus {
        return  try {
            OrderStatus.valueOf(status.uppercase())
        }catch (e : IllegalArgumentException){
            throw IllegalArgumentException("invalid status")
        }
    }
}
