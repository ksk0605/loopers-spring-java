package com.loopers.domain.commerceevent;

import java.util.Map;

public class CommerceEventCommand {
    public record Log(
        String eventType,
        String aggregateId,
        Map<String, Object> payload) {
    }
}
