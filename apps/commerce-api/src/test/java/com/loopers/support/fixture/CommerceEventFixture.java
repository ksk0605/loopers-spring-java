package com.loopers.support.fixture;

import java.util.Map;
import java.util.UUID;

import com.loopers.domain.commerceevent.CommerceEvent;

public class CommerceEventFixture {
    private String eventId = UUID.randomUUID().toString();
    private String aggregateId = "1";
    private String eventType = "COMMERCE_EVENT";
    private Map<String, Object> payload = Map.of(
        "type", "CommerceEvent",
        "data", "testData"
    );

    public static CommerceEventFixture aCommerceEvent() {
        return new CommerceEventFixture();
    }

    public CommerceEventFixture eventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public CommerceEventFixture aggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public CommerceEventFixture eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public CommerceEventFixture payload(Map<String, Object> payload) {
        this.payload = payload;
        return this;
    }

    public CommerceEvent build() {
        return new CommerceEvent(eventId, aggregateId, eventType, payload);
    }

    private CommerceEventFixture() {
    }
}
