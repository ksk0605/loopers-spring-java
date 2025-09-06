package com.loopers.infrastructure.commerceevent;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.domain.commerceevent.CommerceEvent;
import com.loopers.domain.commerceevent.CommerceEventRepository;
import com.loopers.domain.commerceevent.CommerceEventStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventCoreRepository implements CommerceEventRepository {
    private final CommerceEventJpaRepository commerceEventJpaRepository;

    @Override
    public CommerceEvent save(CommerceEvent commerceEvent) {
        return commerceEventJpaRepository.save(commerceEvent);
    }

    @Override
    public List<CommerceEvent> findPendingEvents() {
        return commerceEventJpaRepository.findAllByStatus(CommerceEventStatus.PENDING);
    }
}
