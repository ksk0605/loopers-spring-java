package com.loopers.domain.like;

import com.loopers.domain.usersignal.TargetType;

public class LikeEvent {
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
}
