package edu.example.learner_kotlin.courseabout.order.exception




class OrderTaskException(message: String, val statusCode: Int) : RuntimeException(message)

