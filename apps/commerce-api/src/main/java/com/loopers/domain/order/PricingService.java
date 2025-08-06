package com.loopers.domain.order;

import java.math.BigDecimal;
import java.util.List;

import com.loopers.domain.product.Product;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public class PricingService {
    public BigDecimal calculate(Order order, List<Product> products) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product product = products.stream()
                .filter(p -> p.getId().equals(item.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
            BigDecimal price = product.calculatePriceWithOption(item.getProductOptionId(), item.getQuantity());
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }
}
