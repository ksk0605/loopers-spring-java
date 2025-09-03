package com.loopers.domain.commerceevent;

import java.util.UUID;

import com.loopers.domain.commerceevent.CommerceEventCommand.Log;
import com.loopers.domain.commerceevent.CommerceEventCommand.Send;

public abstract class Publishable {
    protected final String eventId;
    protected final String eventType;
    protected final String aggregateId;

    public Publishable(String eventType, String aggregateId) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.aggregateId = aggregateId;
    }

    public abstract Send toSendCommand();

    public abstract Log toLogCommand();
}
