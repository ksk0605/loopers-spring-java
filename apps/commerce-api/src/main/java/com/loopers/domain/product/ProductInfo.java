package com.loopers.domain.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    Long id;
    String name;
    String description;
    Long price;
    String status;
    Long brandId;
    List<String> imageUrls;
    List<ProductOptionInfo> options;

    public static ProductInfo from(Product product) {
        return new ProductInfo(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice().longValue(),
            product.getStatus().name(),
            product.getBrandId(),
            product.getImages().stream().map(ProductImage::getImageUrl).toList(),
            product.getOptions().stream().map(ProductOptionInfo::from).toList()
        );
    }
}
