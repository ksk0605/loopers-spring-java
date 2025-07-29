package com.loopers.domain.order;

import org.springframework.stereotype.Component;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private final ProductRepository productRepository;

    public void validateOrder(Order order) {
        if (order.getItems().isEmpty()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목이 비어있습니다.");
        }

        for (OrderItem item : order.getItems()) {
            validateOrderItem(item);
        }
    }

    private void validateOrderItem(OrderItem item) {
        if(item.getQuantity() <= 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 수량은 1개 이상이어야 합니다.");
        }

        Product product = productRepository.find(item.getProductId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                        "상품을 찾을 수 없습니다. 상품 ID: " + item.getProductId()));

        if (!product.isAvailable()) {
            throw new CoreException(ErrorType.CONFLICT,
                    "현재 판매 중이 아닌 상품입니다. 상품명: " + product.getName());
        }
    }
}
