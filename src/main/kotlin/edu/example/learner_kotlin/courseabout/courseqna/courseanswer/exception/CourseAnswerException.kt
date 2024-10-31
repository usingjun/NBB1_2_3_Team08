package edu.example.learner_kotlin.courseabout.courseqna.courseanswer.exception

enum class CourseAnswerException(val courseAnswerTaskException: CourseAnswerTaskException) {
    NOT_FOUND("Course Answer NOT FOUND", 404),
    NOT_REGISTERED("Course Answer NOT Registered", 400),
    NOT_MODIFIED("Course Answer NOT Modified", 400),
    NOT_REMOVED("Course Answer NOT Removed", 400),
    NOT_FETCHED("Course Answer NOT Fetched", 400);

    constructor(message: String, code: Int) : this(CourseAnswerTaskException(message, code))
}
