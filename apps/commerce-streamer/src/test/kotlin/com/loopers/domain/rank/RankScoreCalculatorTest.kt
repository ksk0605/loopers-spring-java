package com.loopers.domain.rank

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderItem
import com.loopers.domain.order.OrderStatus
import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalEventType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RankScoreCalculatorTest {
    private val calculator = RankScoreCalculator()

    @Test
    fun `유저 시그널 이벤트 목록으로 랭킹 점수를 계산한다`() {
        // arrange
        val userSignalEvents = listOf(
            // 100번 상품: 좋아요 1회, 좋아요 취소 1회, 조회 1회 -> 0.2 - 0.2 + 0.1 = 0.1
            UserSignalEvent(UserSignalEventType.VIEWED, 100L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.UNLIKE, 100L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.LIKE, 100L, TargetType.PRODUCT),
            // 101번 상품: 좋아요 2회 -> 0.2 + 0.2 = 0.4
            UserSignalEvent(UserSignalEventType.LIKE, 101L, TargetType.PRODUCT),
            UserSignalEvent(UserSignalEventType.LIKE, 101L, TargetType.PRODUCT),
        )

        // act
        var scoreMap = calculator.calculateFromUserSignals(userSignalEvents)

        // assert
        assertThat(scoreMap).hasSize(2)
        assertThat(scoreMap[100L]).isEqualTo(0.1)
        assertThat(scoreMap[101L]).isEqualTo(0.4)
    }

    @Test
    fun `주문 건수 기준으로 랭킹 점수를 정확히 계산한다`() {
        // arrange
        val orders = listOf(
            // 주문 1: 상품 101이 1건 포함됨
            createOrder(
                "order-1", listOf(
                    createOrderItem(productId = 101L, price = BigDecimal(50000), quantity = 2)
                )
            ),
            // 주문 2: 상품 101이 1건, 상품 202가 1건 포함됨
            createOrder(
                "order-2", listOf(
                    createOrderItem(productId = 101L, price = BigDecimal(1000), quantity = 10),
                    createOrderItem(productId = 202L, price = BigDecimal(99000), quantity = 1)
                )
            ),
            // 주문 3: 상품 303이 1건 포함됨
            createOrder(
                "order-3", listOf(
                    createOrderItem(productId = 303L, price = BigDecimal(500), quantity = 5)
                )
            )
        )

        // act
        val scoreMap = calculator.calculateFromOrders(orders)

        // assert
        // 상품 101: 2건의 주문에 포함되었으므로 0.6 * 2 = 1.2점
        // 상품 202: 1건의 주문에 포함되었으므로 0.6 * 1 = 0.6점
        // 상품 303: 1건의 주문에 포함되었으므로 0.6 * 1 = 0.6점
        assertThat(scoreMap).hasSize(3)
        assertThat(scoreMap[101L]).isEqualTo(RankWeight.ORDER.score * 2) // 1.2
        assertThat(scoreMap[202L]).isEqualTo(RankWeight.ORDER.score * 1) // 0.6
        assertThat(scoreMap[303L]).isEqualTo(RankWeight.ORDER.score * 1) // 0.6
    }

    private fun createOrder(orderId: String, items: List<OrderItem>): Order {
        return Order(
            id = null,
            userId = 1L,
            orderId = orderId,
            items = items,
            status = OrderStatus.PAYMENT_COMPLETED,
            orderAmount = BigDecimal.ZERO,
            orderDate = java.time.LocalDateTime.now()
        )
    }

    private fun createOrderItem(productId: Long, price: BigDecimal, quantity: Int): OrderItem {
        return OrderItem(
            id = null,
            productId = productId,
            productOptionId = 1L,
            quantity = quantity,
            basePrice = price,
            optionPrice = BigDecimal.ZERO
        )
    }
}
