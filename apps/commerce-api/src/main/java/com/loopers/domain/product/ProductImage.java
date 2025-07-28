package com.loopers.domain.product;

import lombok.Getter;

@Getter
public class ProductImage {

    private String imageUrl;
    private boolean isMain;
    private int sortOrder;

    public ProductImage(String url, boolean isMain, int sortOrder) {
        this.imageUrl = url;
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
