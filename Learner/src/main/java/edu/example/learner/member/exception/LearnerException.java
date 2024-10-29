package edu.example.learner.member.exception;

import org.springframework.http.HttpStatus;

public enum LearnerException {
    NOT_FOUND_EXCEPTION("Not Found", HttpStatus.NOT_FOUND),
    NOT_REGISTERED_EXCEPTION("Not Registered ", HttpStatus.BAD_REQUEST),
    NOT_MODIFIED_EXCEPTION("Not Modified ", HttpStatus.BAD_REQUEST),
    NOT_REMOVED_EXCEPTION("Not Removed ", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus status;

    LearnerException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public LearnerTaskException getTaskException() {
        return new LearnerTaskException(this.message, this.status.value());
    }
}
