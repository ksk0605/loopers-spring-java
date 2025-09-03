package com.loopers.domain.commerceevent;

public interface CommerceEventPublisher {
    void publish(CommerceEventCommand.Send command);
}
