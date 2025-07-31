package com.loopers.domain.product;

import static com.loopers.support.util.RequireUtils.require;
import static com.loopers.support.util.RequireUtils.requireNonEmpty;
import static com.loopers.support.util.RequireUtils.requireNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 255;
    private static final int MINIMUM_DESCRIPTION_LENGTH = 50;
    private static final int MAIN_IMAGE_SORT_ORDER = 0;

    private String name;

    private String description;
    
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    
    private Long categoryId;
    
    private Long brandId;
    
    private LocalDateTime saleStartDate;
    
    private LocalDateTime saleEndDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductImage> images;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<ProductOption> options;

    public Product(
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        Long brandId,
        Long categoryId,
        LocalDateTime saleStartDate) {
        this.name = requireNonEmpty(name, "상품 이름은 필수입니다.");
        this.description = description;
        this.price = requireNotNull(price, "상품 가격은 필수입니다.");
        this.status = requireNotNull(status, "상품 상태는 필수입니다.");
        this.brandId = requireNotNull(brandId, "브랜드 ID는 필수입니다.");
        this.categoryId = requireNotNull(categoryId, "카테고리 ID는 필수입니다.");
        this.images = new ArrayList<>();
        this.saleStartDate = requireNotNull(saleStartDate, "판매 시작일은 필수입니다.");
        this.options = new ArrayList<>();

        if (description != null)
            validateDescription(description);
        validatePrice(price);
        validateSaleStartDate(saleStartDate);
    }

    private void validateDescription(String description) {
        require(!description.isBlank(), "상품 설명은 빈칸으로만 이루어질 수 없습니다.");
        require(description.length() >= MINIMUM_DESCRIPTION_LENGTH,
            String.format("상품 설명은 %s자 이상 작성해야 합니다.", MINIMUM_DESCRIPTION_LENGTH));
        require(description.length() <= MAXIMUM_DESCRIPTION_LENGTH,
            String.format("상품 설명은 최대 %s자만 작성할 수 있습니다.", MAXIMUM_DESCRIPTION_LENGTH));
    }

    private void validatePrice(BigDecimal price) {
        require(price.compareTo(BigDecimal.ZERO) >= 0, "상품 가격은 0원 이상이어야 합니다.");
    }

    private void validateSaleStartDate(LocalDateTime saleStartDate) {
        LocalDateTime now = LocalDateTime.now();
        require(saleStartDate.isAfter(now), "판매 시작일은 현재 시간 이후여야 합니다.");

        LocalDateTime maxFutureDate = now.plusYears(1);
        require(saleStartDate.isBefore(maxFutureDate), "판매 시작일은 1년 이내로 설정해야 합니다.");
    }

    public void changeStatus(ProductStatus newStatus) {
        this.status = requireNotNull(newStatus, "상태는 필수입니다.");
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

    public void endSale(LocalDateTime endDate) {
        require(this.status == ProductStatus.ON_SALE, "판매중인 상품만 판매 종료할 수 있습니다. 현재 상태: " + this.status);
        require(endDate != null, "판매 종료일은 필수입니다.");
        require(endDate.isAfter(LocalDateTime.now()), "판매 종료일은 현재 시간 이후여야 합니다.");
        require(endDate.isAfter(this.saleStartDate), "판매 종료일은 판매 시작일 이후여야 합니다.");

        this.saleEndDate = endDate;
        this.status = ProductStatus.DISCONTINUED;
    }

    public BigDecimal calculatePriceWithOption(Long productOptionId, Integer quantity) {
        ProductOption productOption = options.stream()
            .filter(option -> option.getId().equals(productOptionId))
            .findFirst()
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 상품 옵션입니다."));
        return price.add(productOption.getAdditionalPrice()).multiply(BigDecimal.valueOf(quantity));
    }

    public void addOption(ProductOption productOption) {
        options.add(productOption);
    }
}
