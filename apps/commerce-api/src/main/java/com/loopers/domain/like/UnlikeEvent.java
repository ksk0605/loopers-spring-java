package com.loopers.domain.like;

import com.loopers.domain.usersignal.TargetType;

public class UnlikeEvent {
    public LikeTarget target;

    public UnlikeEvent(LikeTarget target) {
        this.target = target;
    }

    public TargetType getType() {
        return TargetType.from(target.getType().name());
    }

    public Long getTargetId() {
        return target.getId();
    }
}
