package edu.example.learner_kotlin.courseabout.course.exception


class VideoTaskException(message: String, code: Int) : RuntimeException() {
    override val message: String? = null
    private val code = 0
}
