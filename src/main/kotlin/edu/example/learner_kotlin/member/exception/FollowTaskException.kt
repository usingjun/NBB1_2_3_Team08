package edu.example.learner_kotlin.member.exception

class FollowTaskException(message: String?, val statusCode: Int) : RuntimeException(message)
