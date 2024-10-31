package edu.example.learner_kotlin.courseabout.exception

class NotFoundException : RuntimeException {
    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}