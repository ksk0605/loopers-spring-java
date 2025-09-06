package com.loopers.infrastructure.commerceevent.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loopers.domain.commerceevent.CommerceEvent;
import com.loopers.domain.commerceevent.EventType;

@Component
public class TopicFactory {
    @Value("${kafka.topic.user-signals.topic}")
    private String userSignalsTopic;

    @Value("${kafka.topic.user-signals.version}")
    private String userSignalsVersion;

    @Value("${kafka.topic.payment.topic}")
    private String paymentTopic;

    @Value("${kafka.topic.payment.version}")
    private String paymentVersion;

    public String getTopic(CommerceEvent event) {
        return switch (event.getEventType()) {
            case EventType.LIKE -> userSignalsTopic;
            case EventType.UNLIKE -> userSignalsTopic;
            case EventType.VIEWED -> userSignalsTopic;
            case EventType.PAYMENT_SUCCESS -> paymentTopic;
        };
    }

    public String getVersion(CommerceEvent event) {
        return switch (event.getEventType()) {
            case EventType.LIKE -> userSignalsVersion;
            case EventType.UNLIKE -> userSignalsVersion;
            case EventType.VIEWED -> userSignalsVersion;
            case EventType.PAYMENT_SUCCESS -> paymentVersion;
        };
    }
}
