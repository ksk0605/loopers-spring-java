package com.loopers.domain.inventory;

import java.util.List;

public class InventoryCommand {
    public record Deduct(List<Option> options) {
    }

    public record Option(Long productId, Long productOptionId, Integer quantity) {

    }
}
