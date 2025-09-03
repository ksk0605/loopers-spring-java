package com.loopers.domain.usersignal

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException

class UserSignalTest {
    @DisplayName("좋아요 수가 0이면 감소할 수 없다.")
    @Test
    fun decreaseLikeCount() {
        // arrange
        val signal = UserSignal(null, 1L, TargetType.PRODUCT, 0L, 0L)
        
        // ack & assert
        assertThrows<IllegalStateException> { signal.decreaseLikeCount() }
    }
}
