package com.loopers.infrastructure.brand;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandCacheRepository;
import com.loopers.infrastructure.redis.CacheKeyGenerator;
import com.loopers.infrastructure.redis.CacheVersionManager;
import com.loopers.infrastructure.redis.RedisCacheRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BrandCacheRepositoryImpl implements BrandCacheRepository {
    private static final Duration BRAND_TTL = Duration.ofHours(6);
    
    private final RedisCacheRepository redisCacheRepository;
    private final CacheKeyGenerator cacheKeyGenerator;
    private final CacheVersionManager cacheVersionManager;

    @Override
    public Optional<Brand> getBrand(Long id) {
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.BRAND_PREFIX, id);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.BRAND_PREFIX, id, currentVersion);
        return redisCacheRepository.get(key, Brand.class);
    }

    @Override
    public void setBrand(Long id, Brand brand) {
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.BRAND_PREFIX, id);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.BRAND_PREFIX, id, currentVersion);
        redisCacheRepository.set(key, brand, BRAND_TTL);
    }

    @Override
    public void invalidateBrand(Long id) {
        String pattern = cacheKeyGenerator.generatePattern(CacheKeyGenerator.BRAND_PREFIX, id);
        redisCacheRepository.deleteByPattern(pattern);
        cacheVersionManager.incrementDomainVersion(CacheKeyGenerator.BRAND_PREFIX);
    }
}
