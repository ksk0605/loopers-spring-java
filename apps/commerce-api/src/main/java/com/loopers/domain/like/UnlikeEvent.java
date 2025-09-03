package com.loopers.domain.like;

import java.util.Map;

import com.loopers.domain.commerceevent.CommerceEventCommand;
import com.loopers.domain.commerceevent.Publishable;
import com.loopers.domain.usersignal.TargetType;

public class UnlikeEvent extends Publishable {
    private static final String EVENT_TYPE_PREFIX = "UNLIKE";
    public LikeTarget target;

    public UnlikeEvent(LikeTarget target) {
        super(EVENT_TYPE_PREFIX, target.getId().toString());
        this.target = target;
    }

    public TargetType getType() {
        return TargetType.from(target.getType().name());
    }

    public Long getTargetId() {
        return target.getId();
    }

    @Override
    public CommerceEventCommand.Log toLogCommand() {
        return new CommerceEventCommand.Log(
            eventId,
            getEventType(),
            getTargetId().toString(),
            getPayload()
        );
    }

    @Override
    public CommerceEventCommand.Send toSendCommand() {
        return new CommerceEventCommand.Send(
            eventId,
            getTargetId().toString(),
            getPayload()
        );
    }

    private String getEventType() {
        return EVENT_TYPE_PREFIX + "_" + this.getType().name();
    }

    private Map<String, Object> getPayload() {
        return Map.of(
            "type", getEventType(),
            "targetId", getTargetId(),
            "targetType", getType()
        );
    }
}
