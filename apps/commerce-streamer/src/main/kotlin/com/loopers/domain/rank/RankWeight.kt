package com.loopers.domain.rank

enum class RankWeight(val score: Double) {
    VIEW(0.1),
    LIKE(0.2),
    ORDER(0.6)
}
