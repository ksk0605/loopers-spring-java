package com.loopers.domain.commerceevent

import com.loopers.support.JsonMapConverter
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "commerce_events")
data class CommerceEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "event_id", unique = true, nullable = false)
    val eventId: String,

    @Column(name = "event_type", nullable = false)
    val eventType: String,

    @Column(name = "aggregate_id")
    val aggregateId: String,

    @Column(name = "payload", columnDefinition = "TEXT")
    @Convert(converter = JsonMapConverter::class)
    val payload: Map<String, Any>,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: CommerceEventStatus = CommerceEventStatus.PENDING,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "retry_count")
    val retryCount: Int = 0,
) {
    fun processed(): Boolean {
        return status == CommerceEventStatus.PUBLISHED
    }
}

enum class CommerceEventStatus {
    PENDING,
    PUBLISHED,
    FAILED
}
