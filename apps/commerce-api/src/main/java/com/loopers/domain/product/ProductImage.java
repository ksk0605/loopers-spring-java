package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {
    private String imageUrl;
    private boolean isMain;
    private int sortOrder;

    public ProductImage(String imageUrl, boolean isMain, int sortOrder) {
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
    }

    public void setAsMain() {
        isMain = true;
        sortOrder = 0;
    }

    public boolean isMain() {
        return isMain;
    }

    public String getUrl() {
        return imageUrl;
    }

    public void incrementSortOrder() {
        sortOrder++;
    }

    public void setAsNotMain() {
        isMain = false;
    }
}
