package com.loopers.domain.payment;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.EventType;
import com.loopers.domain.commerceevent.Publishable;

import lombok.Getter;

@Getter
public class PaymentSuccessEvent extends Publishable {
    private final String orderId;

    public PaymentSuccessEvent(String orderId) {
        super(EventType.PAYMENT_SUCCESS.name(), orderId);
        this.orderId = orderId;
    }

    @Override
    public CommerceEventCommand.Record toRecordCommand() {
        return new CommerceEventCommand.Record(
            eventId,
            EventType.PAYMENT_SUCCESS,
            aggregateId,
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "type", EventType.PAYMENT_SUCCESS.name(),
            "orderId", orderId
        );
    }
}
