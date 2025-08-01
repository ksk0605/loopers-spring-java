package com.loopers.application.common;

public record PageInfo(
    int currentPage,
    int pageSize,
    int totalPages,
    long totalElements,
    boolean hasNext
) {
}
