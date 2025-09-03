package com.loopers.domain.common

data class InternalMessage<T>(
    val metadata: Metadata,
    val payload: T
)

data class Metadata(
    val eventId: String,
    val version: String,
    val publishedAt: String
)
