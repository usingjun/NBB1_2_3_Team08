package edu.example.learner_kotlin.member.exception

class MemberTaskException(message: String?, val statusCode: Int) : RuntimeException(message)
