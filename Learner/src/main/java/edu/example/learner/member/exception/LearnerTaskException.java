package edu.example.learner.member.exception;

public class LearnerTaskException extends RuntimeException {
    private final int statusCode;

    public LearnerTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
