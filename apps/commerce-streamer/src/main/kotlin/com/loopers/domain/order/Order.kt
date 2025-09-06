package com.loopers.domain.order

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var userId: Long,
    var orderId: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "order_id")
    val items: List<OrderItem>,
    var status: OrderStatus,
    var orderAmount: BigDecimal,
    var orderDate: LocalDateTime
) {
    fun complete() {
        if (status == OrderStatus.PAYMENT_COMPLETED) {
            throw IllegalStateException("이미 결제 완료된 주문입니다.")
        }
        status = OrderStatus.PAYMENT_COMPLETED
    }
}
