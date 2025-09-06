package com.loopers.domain.product

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "product_option")
data class ProductOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val optionType: String,
    val optionValue: String,
    val additionalPrice: BigDecimal,
    @OneToOne(mappedBy = "productOption")
    val inventory: Inventory
) {
    fun deduct(quantity: Int) {
        inventory.deduct(quantity)
    }
}
