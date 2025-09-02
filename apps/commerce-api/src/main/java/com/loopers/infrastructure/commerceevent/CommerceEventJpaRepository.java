package com.loopers.infrastructure.commerceevent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loopers.domain.commerceevent.CommerceEvent;

public interface CommerceEventJpaRepository extends JpaRepository<CommerceEvent, Long> {

}
