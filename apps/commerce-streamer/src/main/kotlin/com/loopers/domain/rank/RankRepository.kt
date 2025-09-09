package com.loopers.domain.rank

interface RankRepository {
    fun applyScores(scoreMap: Map<Long, Double>)
}
