package edu.example.learner_kotlin.courseabout.exception

import org.springframework.http.HttpStatus

enum class CourseException(private val message: String, private val status: HttpStatus) {
    COURSE_NOT_FOUND("COURSE NOT FOUND", HttpStatus.NOT_FOUND),
    COURSE_ADD_FAILED("COURSE ADD FALIED", HttpStatus.BAD_REQUEST),
    COURSE_NOT_MODIFIED("COURSE NOT MODIFIED", HttpStatus.BAD_REQUEST),
    COURSE_NOT_DELETED("COURSE NOT DELETED", HttpStatus.BAD_REQUEST),
    MEMBER_COURSE_NOT_FOUND("MEMBER COURSE NOT FOUND", HttpStatus.BAD_REQUEST);


    val courseException: CourseTaskException
        get() = CourseTaskException(this.message, status.value())
}
