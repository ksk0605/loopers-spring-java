package com.loopers.domain.product;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPricingService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public BigDecimal calculatePrice(ProductCommand.CalculatePrice command) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (var item : command.options()) {
            Product product = productRepository.find(item.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품입니다."));
            BigDecimal price = product.calculatePriceWithOption(item.productOptionId(), item.quantity());
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }
}
