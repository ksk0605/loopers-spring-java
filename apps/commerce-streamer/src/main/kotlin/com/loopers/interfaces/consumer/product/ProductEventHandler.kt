package com.loopers.interfaces.consumer.product

import com.loopers.domain.product.ProductService
import com.loopers.domain.product.ProductSoldOutEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductEventHandler(
    private val productService: ProductService
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleSoldOut(event: ProductSoldOutEvent) {
        productService.deleteCaches(event)
    }
}
