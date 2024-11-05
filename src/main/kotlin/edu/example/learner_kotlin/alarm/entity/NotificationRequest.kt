package edu.example.learner_kotlin.alarm.entity

data class NotificationRequest(
    val memberId : Long? = null,
    val title : String,
    val message: String,
)
