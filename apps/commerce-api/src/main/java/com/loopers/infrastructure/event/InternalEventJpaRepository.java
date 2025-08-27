package com.loopers.infrastructure.event;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.event.InternalEvent;

public interface InternalEventJpaRepository extends JpaRepository<InternalEvent, Long> {

}
