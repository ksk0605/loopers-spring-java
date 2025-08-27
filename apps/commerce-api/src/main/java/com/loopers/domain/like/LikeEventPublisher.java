package com.loopers.domain.like;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publishLikeEvent(LikeTarget target) {
        publisher.publishEvent(new LikeEvent(target));
    }

    public void publishUnlikeEvent(LikeTarget target) {
        publisher.publishEvent(new UnlikeEvent(target));
    }
}
