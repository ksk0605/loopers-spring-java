package com.loopers.domain.like;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand.Record;
import com.loopers.domain.commerceevent.EventType;
import com.loopers.domain.commerceevent.Publishable;
import com.loopers.domain.usersignal.TargetType;

public class LikeEvent extends Publishable {
    private final Long targetId;
    private final LikeTargetType targetType;

    public LikeEvent(LikeTarget target) {
        super(EventType.LIKE.name(), target.getId().toString());
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
    public Record toRecordCommand() {
        return new Record(
            eventId,
            EventType.LIKE,
            getTargetId().toString(),
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "type", EventType.LIKE.name(),
            "targetId", getTargetId(),
            "targetType", getType()
        );
    }
}
