package edu.example.learner_kotlin.courseabout.courseqna.courseinquiry.exception

class CourseInquiryTaskException(
    override val message: String,
    val code: Int
) : RuntimeException(message)