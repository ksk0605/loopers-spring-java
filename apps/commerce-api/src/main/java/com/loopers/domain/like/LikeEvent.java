package com.loopers.domain.like;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand.Record;
import com.loopers.domain.commerceevent.CommerceEventCommand.Send;
import com.loopers.domain.commerceevent.Publishable;
import com.loopers.domain.usersignal.TargetType;

public class LikeEvent extends Publishable {
    private static final String EVENT_TYPE_PREFIX = "LIKE";
    private final Long targetId;
    private final LikeTargetType targetType;

    public LikeEvent(LikeTarget target) {
        super(EVENT_TYPE_PREFIX, target.getId().toString());
        this.targetId = target.getId();
        this.targetType = target.getType();
    }

    public TargetType getType() {
        return TargetType.from(targetType.name());
    }

    public Long getTargetId() {
        return targetId;
    }

    @Override
    public Send toSendCommand() {
        return new Send(
            eventId,
            getTargetId().toString(),
            getPayload()
        );
    }

    @Override
    public Record toLogCommand() {
        return new Record(
            eventId,
            getEventType(),
            getTargetId().toString(),
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "type", getEventType(),
            "targetId", getTargetId(),
            "targetType", getType()
        );
    }

    private String getEventType() {
        return EVENT_TYPE_PREFIX;
    }
}
