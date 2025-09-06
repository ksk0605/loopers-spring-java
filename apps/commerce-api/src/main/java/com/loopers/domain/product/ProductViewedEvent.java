package com.loopers.domain.product;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.EventType;
import com.loopers.domain.commerceevent.Publishable;

import lombok.Getter;

@Getter
public class ProductViewedEvent extends Publishable {
    private final Long productId;

    public ProductViewedEvent(Long productId) {
        super(EventType.VIEWED.name(), productId.toString());
        this.productId = productId;
    }

    @Override
    public CommerceEventCommand.Record toRecordCommand() {
        return new CommerceEventCommand.Record(
            eventId,
            EventType.VIEWED,
            getProductId().toString(),
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of("productId", getProductId(), "type", EventType.VIEWED.name());
    }
}
