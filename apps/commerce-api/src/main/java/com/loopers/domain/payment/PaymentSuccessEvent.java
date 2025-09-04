package com.loopers.domain.payment;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.Publishable;

import lombok.Getter;

@Getter
public class PaymentSuccessEvent extends Publishable {
    private static final String EVENT_TYPE = "PAYMENT_SUCCESS";
    private final String orderId;

    public PaymentSuccessEvent(String orderId) {
        super(EVENT_TYPE, orderId);
        this.orderId = orderId;
    }

    @Override
    public CommerceEventCommand.Send toSendCommand() {
        return new CommerceEventCommand.Send(
            eventId,
            aggregateId,
            getPayload()
        );
    }

    @Override
    public CommerceEventCommand.Record toRecordCommand() {
        return new CommerceEventCommand.Record(
            eventId,
            EVENT_TYPE,
            aggregateId,
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "orderId", orderId
        );
    }
}
