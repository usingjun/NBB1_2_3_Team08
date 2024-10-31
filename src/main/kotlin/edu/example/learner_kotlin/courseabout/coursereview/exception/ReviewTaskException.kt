package edu.example.learner_kotlin.courseabout.coursereview.exception


class ReviewTaskException(message: String?, val statusCode: Int) : RuntimeException(message){
    override val message: String? = null
    private val code = 0
}
