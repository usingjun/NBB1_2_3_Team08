package edu.example.learner.courseabout.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderTaskException extends RuntimeException {
    private String message;
    private int code;

}
