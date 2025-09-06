package com.loopers.domain.payment

data class PaymentEvent(
    val type: PaymentEventType,
    val orderId: String
)

enum class PaymentEventType {
    PAYMENT_SUCCESS, PAYMENT_FAILURE
}
