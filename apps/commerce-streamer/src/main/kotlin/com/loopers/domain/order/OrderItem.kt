package com.loopers.domain.order

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var productId: Long,
    var productOptionId: Long,
    var quantity: Int,
    var basePrice: BigDecimal,
    var optionPrice: BigDecimal
)
