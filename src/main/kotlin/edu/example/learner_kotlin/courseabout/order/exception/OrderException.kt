package edu.example.learner_kotlin.courseabout.order.exception

import edu.example.learner_kotlin.courseabout.order.exception.OrderTaskException

enum class OrderException(message: String, code: Int) {
    ORDER_NOT_FOUND("ORDER CAN'T FOUNDED", 400),
    NOT_MODIFIED("ORDER NOT MODIFIED", 402),
    NOT_DELETED("ORDER NOT REMOVED", 403),
    FAIL_ADD("ORDER ADD FAILED", 404);


    private val orderTaskException = OrderTaskException(message, code)
    fun get(): OrderTaskException {
        return orderTaskException
    }
}