package com.loopers.domain.order;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_COMPLETED,
    PREPARING_SHIPMENT,
    SHIPPING,
    DELIVERED,
    CANCELLED
}
