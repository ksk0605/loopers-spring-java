package com.loopers.domain.product;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.Publishable;

import lombok.Getter;

@Getter
public class ProductViewedEvent extends Publishable {
    private static final String EVENT_TYPE_PREFIX = "VIEWED";
    private final Long productId;

    public ProductViewedEvent(Long productId) {
        super(EVENT_TYPE_PREFIX, productId.toString());
        this.productId = productId;
    }

    @Override
    public CommerceEventCommand.Log toLogCommand() {
        return new CommerceEventCommand.Log(
            eventId,
            getEventType(),
            getProductId().toString(),
            getPayload()
        );
    }

    @Override
    public CommerceEventCommand.Send toSendCommand() {
        return new CommerceEventCommand.Send(
            eventId,
            getProductId().toString(),
            getPayload()
        );
    }

    private String getEventType() {
        return EVENT_TYPE_PREFIX;
    }

    private Map<String, Object> getPayload() {
        return Map.of("productId", getProductId(), "type", getEventType());
    }
}
