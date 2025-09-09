package com.loopers.domain.common

interface EventDeduplicator {

    /**
     * 해당 eventId가 처음 처리되는 것인지 확인하고, 그렇다면 처리됨으로 기록합니다.
     * @return true: 처음 처리되는 이벤트, false: 이미 처리된 중복 이벤트
     */
    fun checkAndMarkAsProcessed(eventId: String): Boolean
}
