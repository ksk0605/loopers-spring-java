package com.loopers.support.fixture;

import com.loopers.domain.inventory.Inventory;

public class InventoryFixture {
    private Long productId = 1L;
    private Long optionId = 1L;
    private Integer quantity = 10;

    public static InventoryFixture anInventory() {
        return new InventoryFixture();
    }

    public InventoryFixture productId(Long productId) {
        this.productId = productId;
        return this;
    }

    public InventoryFixture optionId(Long optionId) {
        this.optionId = optionId;
        return this;
    }

    public InventoryFixture quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Inventory build() {
        return new Inventory(
            productId,
            optionId,
            quantity
        );
    }

    private InventoryFixture() {
    }
}
