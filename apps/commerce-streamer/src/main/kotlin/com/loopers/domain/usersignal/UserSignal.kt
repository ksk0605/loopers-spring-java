package com.loopers.domain.usersignal

import jakarta.persistence.*

@Entity
@Table(name = "user_signal")
data class UserSignal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val targetId: Long,

    val targetType: TargetType,

    var likeCount: Long,

    var views: Long
) {
    fun increaseLikeCount() {
        likeCount++
    }

    fun decreaseLikeCount() {
        if (likeCount == 0L) {
            throw IllegalStateException("좋아요 수가 0보다 작을 수 없습니다.")
        }
        likeCount--
    }

    fun increaseViews() {
        views++
    }
}

enum class TargetType {
    PRODUCT, BRAND
}
