package com.loopers.application.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.loopers.domain.inventory.InventoryCommand;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductPrice;

public class OrderCriteria {
    public record Order(
        Long userId,
        List<Item> items,
        Long couponId
    ) {
        public ProductCommand.GetAvailable toProductCommand() {
            return new ProductCommand.GetAvailable(
                items.stream().map(Item::toProductOption).toList()
            );
        }

        public OrderCommand.Order toOrderCommandWithProductPrices(List<ProductPrice> productPrices) {
            List<OrderCommand.OrderOption> options = items.stream()
                .map(item -> {
                    ProductPrice productPrice = productPrices.stream()
                        .filter(price -> Objects.equals(price.productId(), item.productId()))
                        .filter(price -> Objects.equals(price.productOptionId(), item.productOptionId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                            String.format("상품(%d)과 옵션(%d)에 대한 가격 정보를 찾을 수 없습니다.",
                                item.productId(), item.productOptionId())
                        ));
                    return item.toOrderOption(productPrice.basePrice(), productPrice.optionPrice());
                })
                .toList();
            return new OrderCommand.Order(userId, options);
        }

        public InventoryCommand.Deduct toInventoryCommand() {
            return new InventoryCommand.Deduct(
                items.stream().map(Item::toInventoryOption).toList()
            );
        }
    }

    public record Item(
        Long productId,
        Long productOptionId,
        Integer quantity
    ) {
        public ProductCommand.OrderOption toProductOption() {
            return new ProductCommand.OrderOption(productId, productOptionId, quantity);
        }

        public OrderCommand.OrderOption toOrderOption(BigDecimal basePrice, BigDecimal optionPrice) {
            return new OrderCommand.OrderOption(productId, productOptionId, quantity, basePrice, optionPrice);
        }

        public InventoryCommand.Option toInventoryOption() {
            return new InventoryCommand.Option(productId, productOptionId, quantity);
        }
    }
}
