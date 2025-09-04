package com.loopers.domain.commerceevent;

import java.util.Map;

public class CommerceEventCommand {
    public record Log(
        String eventId,
        String eventType,
        String aggregateId,
        Map<String, Object> payload) {
    }

    public record Send(
        String eventId,
        String aggregateId,
        Map<String, Object> payload) {

        public static Send from(CommerceEvent event) {
            return new Send(event.getEventId(), event.getAggregateId(), event.getPayload());
        }
    }
}
