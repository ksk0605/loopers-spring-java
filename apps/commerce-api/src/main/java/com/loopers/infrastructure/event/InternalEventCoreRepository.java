package com.loopers.infrastructure.event;

import org.springframework.stereotype.Component;

import com.loopers.domain.event.InternalEvent;
import com.loopers.domain.event.InternalEventRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InternalEventCoreRepository implements InternalEventRepository {
    private final InternalEventJpaRepository internalEventJpaRepository;

    @Override
    public InternalEvent save(InternalEvent event) {
        return internalEventJpaRepository.save(event);
    }
}
