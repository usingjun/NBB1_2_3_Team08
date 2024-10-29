package edu.example.learner.member.exception;

public class LoginTaskException extends RuntimeException {

    private final int statusCode;

    public LoginTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
