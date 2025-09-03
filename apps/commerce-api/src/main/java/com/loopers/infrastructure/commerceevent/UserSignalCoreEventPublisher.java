package com.loopers.infrastructure.commerceevent;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loopers.domain.commerceevent.CommerceEventCommand.Send;
import com.loopers.domain.commerceevent.CommerceEventPublisher;
import com.loopers.infrastructure.commerceevent.kafka.KafkaEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSignalCoreEventPublisher implements CommerceEventPublisher {
    @Value("${kafka.topic.user-signals.topic}")
    private String userSignalsTopic;

    @Value("${kafka.topic.user-signals.version}")
    private String version;

    private final KafkaEventPublisher kafkaEventPublisher;

    @Override
    public void publish(Send command) {
        InternalEvent event = new InternalEvent(
            new InternalEvent.Metadata(command.eventId(), version, LocalDateTime.now().toString()),
            command.payload()
        );
        kafkaEventPublisher.publish(event, userSignalsTopic, command.aggregateId());
    }
}
