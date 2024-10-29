package edu.example.learner.courseabout.order.exception;

public enum OrderException {
    ORDER_NOT_FOUND("ORDER CAN'T FOUNDED",400),
    NOT_MODIFIED("ORDER NOT MODIFIED", 402),
    NOT_DELETED("ORDER NOT REMOVED", 403),
    FAIL_ADD("ORDER ADD FAILED", 404);


    private OrderTaskException orderTaskException;
    OrderException(String message, int code) {
        orderTaskException = new OrderTaskException(message, code);
    }
    public OrderTaskException get(){
        return orderTaskException;
    }
}