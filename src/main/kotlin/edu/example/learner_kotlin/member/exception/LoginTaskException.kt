package edu.example.learner_kotlin.member.exception

class LoginTaskException(message: String?, val statusCode: Int) : RuntimeException(message)
