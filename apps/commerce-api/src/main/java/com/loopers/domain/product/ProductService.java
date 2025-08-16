package com.loopers.domain.product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final ProductCacheRepository productCacheRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public Product get(Long id) {
        return productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + id));
    }

    @Transactional(readOnly = true)
    public ProductInfo getInfo(Long id) {
        Optional<ProductInfo> cachedInfo = productCacheRepository.getProductInfo(id);
        if (cachedInfo.isPresent()) {
            return cachedInfo.get();
        }
        Product product = productRepository.find(id)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다. 상품 ID: " + id));
        ProductInfo info = ProductInfo.from(product);
        productCacheRepository.setProductInfo(id, info);
        return info;
    }

    @Transactional(readOnly = true)
    public List<Product> getAll(List<Long> ids) {
        return productRepository.findAll(ids);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAll(ProductCommand.Search command) {
        return productRepository.findAll(command);
    }

    @Transactional(readOnly = true)
    public List<ProductPrice> getAvailableProductPrices(ProductCommand.GetAvailable command) {
        List<Long> productIds = command.options().stream()
            .map(option -> option.productId())
            .distinct()
            .toList();

        Map<Long, Product> products = productRepository.findAll(productIds)
            .stream()
            .collect(Collectors.toMap(
                Product::getId,
                product -> product));

        if (products.size() < productIds.size()) {
            throw new CoreException(ErrorType.NOT_FOUND, "구매할 상품이 존재하지 않습니다.");
        }

        return productMapper.mapToProductPrices(command.options(), products);
    }
}
