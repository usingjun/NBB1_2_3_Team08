package edu.example.learner.courseabout.exception;

public enum CourseAnswerException {
    NOT_FOUND("Course Answer NOT FOUND", 404),
    NOT_REGISTERED("Course Answer NOT Registered", 400),
    NOT_MODIFIED("Course Answer NOT Modified", 400),
    NOT_REMOVED("Course Answer NOT Removed", 400),
    NOT_FETCHED("Course Answer NOT Fetched",400);

    private CourseAnswerTaskException courseAnswerTaskException;

    CourseAnswerException(String message, int code) {
        courseAnswerTaskException = new CourseAnswerTaskException(message, code);
    }

    public CourseAnswerTaskException get() {
        return courseAnswerTaskException;
    }
}
