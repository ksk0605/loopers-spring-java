package com.loopers.infrastructure.commerceevent;

import java.util.Map;

public record InternalMessage(
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
