package edu.example.learner_kotlin.token.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "token", timeToLive = 86400)
data class RefreshEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val username: String,

    val refreshToken: String
)