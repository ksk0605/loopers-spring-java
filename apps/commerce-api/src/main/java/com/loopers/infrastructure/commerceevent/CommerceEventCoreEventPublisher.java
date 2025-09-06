package com.loopers.infrastructure.commerceevent;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.loopers.domain.commerceevent.CommerceEvent;
import com.loopers.domain.commerceevent.CommerceEventPublisher;
import com.loopers.infrastructure.commerceevent.kafka.KafkaEventPublisher;
import com.loopers.infrastructure.commerceevent.kafka.TopicFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommerceEventCoreEventPublisher implements CommerceEventPublisher {
    private final KafkaEventPublisher kafkaEventPublisher;
    private final TopicFactory topicFactory;

    @Override
    public void publish(CommerceEvent event) {
        InternalMessage message = new InternalMessage(
            new InternalMessage.Metadata(event.getEventId(), topicFactory.getVersion(event), LocalDateTime.now().toString()),
            event.getPayload()
        );
        kafkaEventPublisher.publish(message, topicFactory.getTopic(event), event.getAggregateId());
    }
}
