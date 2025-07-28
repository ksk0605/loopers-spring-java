package com.loopers.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.Getter;

@Getter
public class Product {
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 255;
    private static final int MINIMUM_DESCRIPTION_LENGTH = 50;
    private static final int MAIN_IMAGE_SORT_ORDER = 0;

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private Long categoryId;
    private Long brandId;
    private List<ProductImage> images;

    public Product(String name, String description, BigDecimal price, ProductStatus status, Long brandId, Long categoryId) {
        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateStatus(status);
        validateBrandId(brandId);
        validateCategoryId(categoryId);

        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.images = new ArrayList<>();
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 이름은 필수입니다.");
        }
    }

    private void validateDescription(String description) {
        if (description == null) {
            return;
        }
        if (description.isBlank()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 설명은 빈칸으로만 이루어질 수 없습니다.");
        }
        if (description.length() < MINIMUM_DESCRIPTION_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST,
                String.format("상품 설명은 %s자 이상 작성해야 합니다.", MINIMUM_DESCRIPTION_LENGTH));
        }
        if (description.length() > MAXIMUM_DESCRIPTION_LENGTH) {
            throw new CoreException(ErrorType.BAD_REQUEST,
                String.format("상품 설명은 최대 %s자만 작성할 수 있습니다.", MAXIMUM_DESCRIPTION_LENGTH));
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 필수입니다.");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 가격은 0원 이상이어야 합니다.");
        }
    }

    private void validateStatus(ProductStatus status) {
        if (status == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "상품 상태는 필수입니다.");
        }
    }

    private void validateBrandId(Long brandId) {
        if (brandId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "브랜드 ID는 필수입니다.");
        }
    }

    private void validateCategoryId(Long categoryId) {
        if (categoryId == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "카테고리 ID는 필수입니다.");
        }
    }

    public void changeStatus(ProductStatus newStatus) {
        validateStatus(newStatus);
        this.status = newStatus;
    }

    public boolean isAvailable() {
        return this.status == ProductStatus.ON_SALE;
    }

    public void addImage(String url, boolean isMain) {
        if (isMain || images.isEmpty()) {
            addAsMainImage(url);
            return;
        }
        addAsSubImage(url);
    }

    private void addAsMainImage(String url) {
        demoteCurrentMainImages();
        ProductImage productImage = new ProductImage(url, true, MAIN_IMAGE_SORT_ORDER);
        images.add(productImage);
    }

    private void demoteCurrentMainImages() {
        for (ProductImage image : images) {
            image.setAsNotMain();
            image.incrementSortOrder();
        }
    }

    private void addAsSubImage(String url) {
        images.add(new ProductImage(url, false, images.size()));
    }

    public List<ProductImage> getImages() {
        return images.stream()
            .sorted(Comparator.comparingInt(ProductImage::getSortOrder))
            .toList();
    }
}
