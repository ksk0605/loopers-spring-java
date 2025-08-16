package com.loopers.domain.product;

import java.util.Optional;

import com.loopers.application.product.ProductResults;

public interface ProductCacheRepository {
    Optional<ProductInfo> getProductInfo(Long id);

    void setProductInfo(Long id, ProductInfo productInfo);

    Optional<ProductResults> getProductResults(ProductCommand.Search command);

    void setProductResults(ProductCommand.Search command, ProductResults productResults);
}
