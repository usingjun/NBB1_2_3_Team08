package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.exception

enum class CourseInquiryException(val courseInquiryTaskException: CourseInquiryTaskException) {
    NOT_FOUND("Course Inquiry NOT FOUND", 404),
    NOT_REGISTERED("Course Inquiry NOT Registered", 400),
    NOT_MODIFIED("Course Inquiry NOT Modified", 400),
    NOT_REMOVED("Course Inquiry NOT Removed", 400),
    NOT_FETCHED("Course Inquiry NOT Fetched", 400);

    constructor(message: String, code: Int) : this(CourseInquiryTaskException(message, code))
}
