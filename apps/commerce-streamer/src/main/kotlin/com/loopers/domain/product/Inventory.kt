package com.loopers.domain.product

import jakarta.persistence.*

@Entity
@Table(name = "inventory")
data class Inventory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val productId: Long,
    @OneToOne
    @JoinColumn(name = "product_option_id")
    val productOption: ProductOption,
    var quantity: Int,
    var reservedQuantity: Int,
) {
    fun deduct(quantity: Int) {
        require(quantity <= reservedQuantity && quantity <= this.quantity)
        this.reservedQuantity -= quantity
        this.quantity -= quantity
    }
}
