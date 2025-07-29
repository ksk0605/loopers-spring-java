package com.loopers.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductSummary {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Long likeCount;
    private Long brandId;
    private String brandName;
    private String brandDescription;
    private String brandLogoUrl;
    private List<ProductImage> images;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;
}
