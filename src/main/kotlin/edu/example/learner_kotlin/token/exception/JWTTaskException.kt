package edu.example.learner_kotlin.token.exception

class JWTTaskException(message: String?, val statusCode: Int) : RuntimeException(message)
