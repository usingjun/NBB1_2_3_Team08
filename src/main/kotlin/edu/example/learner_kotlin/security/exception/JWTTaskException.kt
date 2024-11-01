package edu.example.learner_kotlin.security.exception

class JWTTaskException(message: String?, val statusCode: Int) : RuntimeException(message)
