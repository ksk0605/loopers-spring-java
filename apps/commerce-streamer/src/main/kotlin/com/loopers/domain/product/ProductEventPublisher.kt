package com.loopers.domain.product

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProductEventPublisher(
    private val publisher: ApplicationEventPublisher
) {
    fun publishSoldOutEvent(productIds: List<Long>) {
        publisher.publishEvent(ProductSoldOutEvent(productIds))
    }
}
