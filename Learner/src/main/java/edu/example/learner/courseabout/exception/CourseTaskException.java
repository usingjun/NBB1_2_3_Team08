package edu.example.learner.courseabout.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CourseTaskException extends RuntimeException {

    private final int statusCode;

    public CourseTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
