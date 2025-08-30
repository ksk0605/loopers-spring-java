package com.loopers.domain.event;

import java.time.LocalDateTime;
import java.util.Map;

import com.loopers.support.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "internal_events")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class InternalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "attributes", columnDefinition = "TEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> attributes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InternalEvent(String eventType, Map<String, Object> attributes) {
        this.eventType = eventType;
        this.attributes = attributes;
        this.createdAt = LocalDateTime.now();
    }
}
