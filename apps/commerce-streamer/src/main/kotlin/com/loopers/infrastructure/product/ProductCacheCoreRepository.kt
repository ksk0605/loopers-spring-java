package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductCacheRepository
import com.loopers.infrastructure.redis.RedisCacheRepository
import org.springframework.stereotype.Component

@Component
class ProductCacheCoreRepository(
    private val redisCacheRepository: RedisCacheRepository
) : ProductCacheRepository {

    override fun deleteProductInfo(id: Long) {
        redisCacheRepository.delete(id.toString())
    }
}
