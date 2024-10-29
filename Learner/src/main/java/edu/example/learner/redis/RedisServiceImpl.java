package edu.example.learner.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setValues(String key, String value) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    @Override
    public void setValues(String key, String value, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    @Override
    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) return "";
        return String.valueOf(values.get(key));
    }

    @Override
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    // 중복 조회 여부 확인
    public boolean isDuplicateView(String key, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(hasKey)) {
            return true;  // 이미 조회된 경우
        } else {
            values.set(key, "viewed", duration);  // 조회 정보 Redis에 저장
            return false;  // 중복 조회 아님
        }
    }
}
