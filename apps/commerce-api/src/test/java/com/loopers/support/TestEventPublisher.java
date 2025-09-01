package com.loopers.support;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TestEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
