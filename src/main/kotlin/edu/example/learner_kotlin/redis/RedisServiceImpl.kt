package edu.example.learner_kotlin.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisServiceImpl(
    private val redisTemplate: RedisTemplate<String, Any>
) : RedisService {

    override fun setValues(key: String, value: String) {
        redisTemplate.opsForValue().set(key, value)
    }

    override fun setValues(key: String, value: String, duration: Duration) {
        redisTemplate.opsForValue().set(key, value, duration)
    }

    override fun getValue(key: String): String {
        val value = redisTemplate.opsForValue().get(key)
        return value?.toString() ?: ""
    }

    override fun deleteValue(key: String) {
        redisTemplate.delete(key)
    }

    // 중복 조회 여부 확인
    fun isDuplicateView(key: String, duration: Duration): Boolean {
        return if (redisTemplate.hasKey(key) == true) {
            true // 이미 조회된 경우
        } else {
            redisTemplate.opsForValue().set(key, "viewed", duration) // 조회 정보 Redis에 저장
            false // 중복 조회 아님
        }
    }
}