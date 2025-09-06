package com.loopers.infrastructure.redis;

import org.springframework.stereotype.Component;

@Component
public class CacheKeyGenerator {

    public static final String BRAND_PREFIX = "brand";
    public static final String PRODUCT_PREFIX = "product";
    public static final String PRODUCTS_PREFIX = "products";
    public static final String INVENTORY_PREFIX = "inventory";

    public String generateVersionedKey(String prefix, Long id, String version) {
        return String.format("%s:%d:v%s", prefix, id, version);
    }

    public String generateVersionedKey(String prefix, Long id, Long version) {
        return String.format("%s:%d:v%d", prefix, id, version);
    }

    public String generateVersionedKey(String prefix, Object... params) {
        StringBuilder key = new StringBuilder(prefix);
        for (Object param : params) {
            key.append(":").append(param);
        }
        return key.toString();
    }

    public String generatePattern(String prefix, Long id) {
        return String.format("%s:%d:v*", prefix, id);
    }
}
