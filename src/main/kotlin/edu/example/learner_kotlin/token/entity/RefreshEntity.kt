package edu.example.learner_kotlin.token.entity


import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "token", timeToLive = 86400)
data class RefreshEntity(
    @Id
    val refreshToken: String,

    val username: String
)