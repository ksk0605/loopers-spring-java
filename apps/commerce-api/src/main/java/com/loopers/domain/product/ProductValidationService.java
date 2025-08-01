package com.loopers.domain.product;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductValidationService {
    private final ProductRepository productRepository;

    public void validate(Long productId) {
        Product product = productRepository.find(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                        "상품을 찾을 수 없습니다. 상품 ID: " + productId));

        if (!product.isAvailable()) {
            throw new CoreException(ErrorType.CONFLICT,
                    "현재 판매 중이 아닌 상품입니다. 상품명: " + product.getName());
        }
    }
}
