package edu.example.learner_kotlin.redis

import org.springframework.stereotype.Service
import java.time.Duration

@Service
interface RedisService {
    // 값 등록 / 수정
    fun setValues(key: String, value: String)

    // 값 등록 / 수정 (유효 기간 설정)
    fun setValues(key: String, value: String, duration: Duration)

    // 값 조회
    fun getValue(key: String): String?

    // 값 삭제
    fun deleteValue(key: String)
}