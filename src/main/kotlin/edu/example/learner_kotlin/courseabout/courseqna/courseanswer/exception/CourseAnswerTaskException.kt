package edu.example.learner_kotlin.courseabout.courseqna.courseanswer.exception

class CourseAnswerTaskException(
    override val message: String,
    val code: Int
) : RuntimeException(message)