package com.loopers.infrastructure.product;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.application.product.ProductResults;
import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductCommand.Search;
import com.loopers.domain.product.ProductInfo;
import com.loopers.infrastructure.redis.CacheKeyGenerator;
import com.loopers.infrastructure.redis.CacheVersionManager;
import com.loopers.infrastructure.redis.RedisCacheRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCacheRepositoryImpl implements ProductCacheRepository {
    private static final Duration PRODUCT_TTL = Duration.ofHours(2);
    private static final Duration PRODUCTS_TTL = Duration.ofMinutes(1);

    private final RedisCacheRepository redisCacheRepository;
    private final CacheKeyGenerator cacheKeyGenerator;
    private final CacheVersionManager cacheVersionManager;

    @Override
    public Optional<ProductInfo> getProductInfo(Long id) {
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.PRODUCT_PREFIX, id);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.PRODUCT_PREFIX, id, currentVersion);
        return redisCacheRepository.get(key, ProductInfo.class);
    }

    @Override
    public void setProductInfo(Long id, ProductInfo productInfo) {
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.PRODUCT_PREFIX, id);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.PRODUCT_PREFIX, id, currentVersion);
        redisCacheRepository.set(key, productInfo, PRODUCT_TTL);
    }

    @Override
    public Optional<ProductResults> getProductResults(Search command) {
        Long commandHash = (long) Objects.hash(command.sortBy(), command.page(), command.size(), command.status());
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.PRODUCTS_PREFIX, commandHash);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.PRODUCTS_PREFIX, commandHash,
                currentVersion);
        return redisCacheRepository.get(key, ProductResults.class);
    }

    @Override
    public void setProductResults(Search command, ProductResults productResults) {
        Long commandHash = (long) Objects.hash(command.sortBy(), command.page(), command.size(), command.status());
        Long currentVersion = cacheVersionManager.getCurrentVersion(CacheKeyGenerator.PRODUCTS_PREFIX, commandHash);
        String key = cacheKeyGenerator.generateVersionedKey(CacheKeyGenerator.PRODUCTS_PREFIX, commandHash,
                currentVersion);
        redisCacheRepository.set(key, productResults, PRODUCTS_TTL);
    }
}
