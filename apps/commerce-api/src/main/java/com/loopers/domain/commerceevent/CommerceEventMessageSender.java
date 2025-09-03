package com.loopers.domain.commerceevent;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventMessageSender {
    private final CommerceEventPublisher commerceEventPublisher;

    public void sendUserSignalsEvent(CommerceEventCommand.Send command) {
        commerceEventPublisher.publish(command);
    }
}
