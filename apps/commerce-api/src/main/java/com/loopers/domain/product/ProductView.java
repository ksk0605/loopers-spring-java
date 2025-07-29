package com.loopers.domain.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductView {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Long likeCount;
    private Brand brand;
    private List<ProductImage> images;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Brand {
        private Long id;
        private String name;
        private String description;
        private String logoUrl;
    }
}
