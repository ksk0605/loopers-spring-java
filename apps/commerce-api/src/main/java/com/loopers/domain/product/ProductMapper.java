package com.loopers.domain.product;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public List<ProductPrice> mapToProductPrices(List<ProductCommand.OrderOption> orderOptions, Map<Long, Product> productMap) {
        return orderOptions.stream()
            .map(option -> toProductPrice(option, productMap.get(option.productId())))
            .toList();
    }

    private ProductPrice toProductPrice(ProductCommand.OrderOption orderOption, Product product) {
        ProductOption productOption = product.getOptions().stream()
            .filter(opt -> opt.getId().equals(orderOption.productOptionId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다"));

        return new ProductPrice(
            product.getId(),
            productOption.getId(),
            product.getPrice(),
            productOption.getAdditionalPrice()
        );
    }
}
