package com.loopers.domain.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductInfo get(Long id) {
        Product product = productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + id));
        return ProductInfo.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductInfo> getAll(List<Long> ids) {
        return productRepository.findAll(ids)
            .stream()
            .map(ProductInfo::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductInfo> getAll(ProductCommand.Search command) {
        Page<Product> products = productRepository.findAll(command);
        return products.map(ProductInfo::from);
    }

    public List<ProductPrice> getAvailableProductPrices(ProductCommand.GetAvailable command) {
        List<Long> productIds = command.options().stream()
            .map(option -> option.productId())
            .distinct()
            .toList();

        Map<Long, Product> products = productRepository.findAll(productIds)
            .stream()
            .collect(Collectors.toMap(
                Product::getId,
                product -> product
            ));

        if (products.size() < productIds.size()) {
            throw new CoreException(ErrorType.NOT_FOUND, "구매할 상품이 존재하지 않습니다.");
        }

        return productMapper.mapToProductPrices(command.options(), products);
    }
}
