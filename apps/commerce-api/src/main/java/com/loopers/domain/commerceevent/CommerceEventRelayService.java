package com.loopers.domain.commerceevent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventRelayService {
    private final CommerceEventRepository commerceEventRepository;
    private final CommerceEventPublisher commerceEventPublisher;

    @Scheduled(fixedDelay = 180, initialDelay = 180, timeUnit = TimeUnit.SECONDS)
    public void relay() {
        List<CommerceEvent> events = commerceEventRepository.findPendingEvents();
        events.forEach(event -> {
            commerceEventPublisher.publish(event);
        });
    }
}
