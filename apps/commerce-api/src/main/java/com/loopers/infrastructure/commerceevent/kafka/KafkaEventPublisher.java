package com.loopers.infrastructure.commerceevent.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.loopers.infrastructure.commerceevent.InternalEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void publish(InternalEvent event, String topic, String key) {
        kafkaTemplate.send(topic, key, event);
    }
}
