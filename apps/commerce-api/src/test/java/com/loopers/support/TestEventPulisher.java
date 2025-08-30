package com.loopers.support;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TestEventPulisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TestEventPulisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
