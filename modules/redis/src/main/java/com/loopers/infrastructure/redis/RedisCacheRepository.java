package com.loopers.infrastructure.redis;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(value).map(clazz::cast);
    }

    public <T> Optional<List<T>> getList(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Arrays.asList(value).stream().map(clazz::cast).toList());
    }

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void deleteByPattern(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern));
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public void incrementScore(String key, Object member, double score) {
        redisTemplate.opsForZSet().incrementScore(key, member, score);
    }

    public void expire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }

    public Boolean setIfAbsent(String key, Object value, Duration ttl) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue().setIfAbsent(key, value, ttl)
        );
    }
}
