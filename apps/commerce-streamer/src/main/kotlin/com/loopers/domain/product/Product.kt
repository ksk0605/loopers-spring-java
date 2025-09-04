package com.loopers.domain.product

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var name: String,
    var description: String,
    var price: BigDecimal,
    @Enumerated(EnumType.STRING)
    var status: ProductStatus,
    var categoryId: Long?,
    var brandId: Long,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "product_id")
    var options: List<ProductOption>
) {
    fun deduct(productOptionId: Long, quantity: Int) {
        val productOption = options.find { it.id == productOptionId } ?: throw IllegalArgumentException("존재하지 않는 옵션입니다.")
        productOption.deduct(quantity)
    }
}

enum class ProductStatus {
    ON_SALE,
    SOLD_OUT,
    DISCONTINUED
}
