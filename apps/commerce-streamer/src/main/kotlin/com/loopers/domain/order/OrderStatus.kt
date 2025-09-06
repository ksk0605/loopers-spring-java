package com.loopers.domain.order

enum class OrderStatus {
    PENDING,
    PENDING_PAYMENT,
    PAYMENT_COMPLETED,
    PREPARING_SHIPMENT,
    SHIPPING,
    DELIVERED,
    CANCELLED
}
