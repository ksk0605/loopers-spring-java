package com.loopers.infrastructure.commerceevent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.commerceevent.CommerceEvent;
import com.loopers.domain.commerceevent.CommerceEventStatus;

public interface CommerceEventJpaRepository extends JpaRepository<CommerceEvent, Long> {
    List<CommerceEvent> findAllByStatus(CommerceEventStatus commerceEventStatus);
}
