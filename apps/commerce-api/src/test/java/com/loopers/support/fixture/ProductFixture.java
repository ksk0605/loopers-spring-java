package com.loopers.support.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductStatus;

public class ProductFixture {
    private String name = "테스트 상품";
    private String description = "테스트 상품 설명입니다. 이 상품은 매우 멋지고 매력적입니다. 지금 바로 구매하셔야 합니다.";
    private BigDecimal price = BigDecimal.valueOf(20000);
    private ProductStatus status = ProductStatus.ON_SALE;
    private Long categoryId = 1L;
    private Long brandId = 1L;
    private LocalDateTime saleStartDate = LocalDateTime.now().plusDays(3);

    public static ProductFixture aProduct() {
        return new ProductFixture();
    }

    public ProductFixture name(String name) {
        this.name = name;
        return this;
    }

    public ProductFixture description(String description) {
        this.description = description;
        return this;
    }

    public ProductFixture price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductFixture status(ProductStatus status) {
        this.status = status;
        return this;
    }

    public ProductFixture categoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public ProductFixture brandId(Long brandId) {
        this.brandId = brandId;
        return this;
    }

    public ProductFixture saleStartDate(LocalDateTime saleStartDate) {
        this.saleStartDate = saleStartDate;
        return this;
    }

    public Product build() {
        return new Product(
            name,
            description,
            price,
            status,
            brandId,
            categoryId,
            saleStartDate
        );
    }
}
