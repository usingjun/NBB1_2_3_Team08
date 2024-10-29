package edu.example.learner.courseabout.exception;

public enum CourseInquiryException {
    NOT_FOUND("Course Inquiry NOT FOUND", 404),
    NOT_REGISTERED("Course Inquiry NOT Registered", 400),
    NOT_MODIFIED("Course Inquiry NOT Modified", 400),
    NOT_REMOVED("Course Inquiry NOT Removed", 400),
    NOT_FETCHED("Course Inquiry NOT Fetched",400);

    private CourseInquiryTaskException courseInquiryTaskException;

    CourseInquiryException(String message, int code) {
        courseInquiryTaskException = new CourseInquiryTaskException(message, code);
    }

    public CourseInquiryTaskException get() {
        return courseInquiryTaskException;
    }
}
