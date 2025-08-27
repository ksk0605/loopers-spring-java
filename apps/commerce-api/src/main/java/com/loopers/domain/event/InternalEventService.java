package com.loopers.domain.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InternalEventService {

    private final InternalEventRepository internalEventRepository;

    @Transactional
    public void log(Loggable loggable) {
        InternalEvent internalEvent = loggable.toInternalEvent();
        internalEventRepository.save(internalEvent);
    }
}
