package com.loopers.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheVersionManager {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String VERSION_KEY_PREFIX = "cache:version";

    // 특정 도메인의 버전 증가
    public Long incrementVersion(String domain, Long id) {
        String versionKey = String.format("%s:%s:%d", VERSION_KEY_PREFIX, domain, id);
        return redisTemplate.opsForValue().increment(versionKey);
    }

    // 특정 도메인의 현재 버전 조회
    public Long getCurrentVersion(String domain, Long id) {
        String versionKey = String.format("%s:%s:%d", VERSION_KEY_PREFIX, domain, id);
        String version = redisTemplate.opsForValue().get(versionKey);
        return version != null ? Long.parseLong(version) : 1L;
    }

    // 도메인 전체 버전 증가 (전체 캐시 무효화)
    public Long incrementDomainVersion(String domain) {
        String versionKey = String.format("%s:%s:global", VERSION_KEY_PREFIX, domain);
        return redisTemplate.opsForValue().increment(versionKey);
    }

    // 전체 캐시 버전 증가
    public Long incrementGlobalVersion() {
        String versionKey = VERSION_KEY_PREFIX + ":global";
        return redisTemplate.opsForValue().increment(versionKey);
    }
}
