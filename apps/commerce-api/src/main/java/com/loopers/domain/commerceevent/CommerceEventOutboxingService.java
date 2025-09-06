package com.loopers.domain.commerceevent;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class CommerceEventOutboxingService {
    private final CommerceEventRepository commerceEventRepository;

    public void record(CommerceEventCommand.Record command) {
        CommerceEvent commerceEvent = CommerceEvent.from(command);
        commerceEventRepository.save(commerceEvent);
    }
}
