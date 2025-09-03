package com.loopers.infrastructure.commerceevent;

import java.util.Map;

public record InternalEvent(
    Metadata metadata,
    Map<String, Object> payload
) {
    public record Metadata(
        String eventId,
        String version,
        String publishedAt
    ) {
    }
}
