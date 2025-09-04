package com.loopers.domain.inventory;

import java.util.List;

public class InventoryCommand {
    public record Deduct(List<Option> options) {
        public List<Long> productIds() {
            return options.stream().map(Option::productId).toList();
        }

        public List<Long> productOptionIds() {
            return options.stream().map(Option::productOptionId).toList();
        }
    }

    public record Reverse(List<Option> options) {
        public List<Long> productIds() {
            return options.stream().map(Option::productId).toList();
        }

        public List<Long> productOptionIds() {
            return options.stream().map(Option::productOptionId).toList();
        }
    }

    public record Option(Long productId, Long productOptionId, Integer quantity) {

    }
}
