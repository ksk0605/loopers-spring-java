package com.loopers.support.fixture;

import java.math.BigDecimal;

import com.loopers.domain.payment.PaymentEvent;

public class PaymentEventFixture {

    private String orderId = "1234567890";
    private BigDecimal amount = BigDecimal.valueOf(10000);
    private String buyerId = "userId";

    public static PaymentEventFixture aPaymentEvent() {
        return new PaymentEventFixture();
    }

    public PaymentEventFixture orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public PaymentEventFixture amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentEventFixture buyerId(String buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    public PaymentEvent build() {
        return new PaymentEvent(orderId, amount, buyerId);
    }

    private PaymentEventFixture() {
    }
}
