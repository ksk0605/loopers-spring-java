package com.loopers.domain.rank

import org.springframework.stereotype.Component

@Component
class RankService(
    private val rankRepository: RankRepository
) {
    fun applyScores(scoreMap: Map<Long, Double>) {
        rankRepository.applyScores(scoreMap)
    }
}
