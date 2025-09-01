package com.loopers.domain.like;

import java.util.Map;

import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.event.Loggable;
import com.loopers.domain.usersignal.TargetType;

public class LikeEvent implements Loggable {
    public LikeTarget target;

    public LikeEvent(LikeTarget target) {
        this.target = target;
    }

    public TargetType getType() {
        return TargetType.from(target.getType().name());
    }

    public Long getTargetId() {
        return target.getId();
    }

    @Override
    public InternalEvent toInternalEvent() {
        return new InternalEvent(
            this.getClass().getSimpleName(),
            Map.of(
                "targetId", this.getTargetId(),
                "targetType", this.getType()
            )
        );
    }
}
