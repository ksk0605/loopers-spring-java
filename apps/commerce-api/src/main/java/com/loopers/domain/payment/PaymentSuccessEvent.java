package com.loopers.domain.payment;

import lombok.Getter;

@Getter
public class PaymentSuccessEvent {
    private final String orderId;

    public PaymentSuccessEvent(String orderId) {
        this.orderId = orderId;
    }
}
