package com.loopers.domain.order;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderPricingService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(Order order) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.find(item.getProductId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품입니다."));
            BigDecimal price = product.calculatePriceWithOption(item.getProductOptionId(), item.getQuantity());
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }
}
