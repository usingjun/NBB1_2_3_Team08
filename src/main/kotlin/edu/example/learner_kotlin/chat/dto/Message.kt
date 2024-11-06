package edu.example.learner_kotlin.chat.dto

data class Message(
    val type: MsgType?,
    val sender: String,
    val content: String,
    val timestamp: Long? = System.currentTimeMillis()
)