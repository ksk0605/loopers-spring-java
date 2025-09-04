package com.loopers.domain.commerceevent;

import java.util.Map;

public class CommerceEventCommand {
    public record Record(
        String eventId,
        EventType eventType,
        String aggregateId,
        Map<String, Object> payload) {
    }
}
