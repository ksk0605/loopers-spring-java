package com.loopers.domain.like;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.EventType;
import com.loopers.domain.commerceevent.Publishable;
import com.loopers.domain.usersignal.TargetType;

public class UnlikeEvent extends Publishable {
    public LikeTarget target;

    public UnlikeEvent(LikeTarget target) {
        super(EventType.UNLIKE.name(), target.getId().toString());
        this.target = target;
    }

    public TargetType getType() {
        return TargetType.from(target.getType().name());
    }

    public Long getTargetId() {
        return target.getId();
    }

    @Override
    public CommerceEventCommand.Record toRecordCommand() {
        return new CommerceEventCommand.Record(
            eventId,
            EventType.UNLIKE,
            getTargetId().toString(),
            getPayload()
        );
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "type", EventType.UNLIKE.name(),
            "targetId", getTargetId(),
            "targetType", getType()
        );
    }
}
