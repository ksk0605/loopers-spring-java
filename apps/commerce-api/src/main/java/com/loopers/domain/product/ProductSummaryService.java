package com.loopers.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductSummaryService {
    private final ProductSummaryRepository productSummaryRepository;

    public Page<ProductSummary> summaries(SummarySearchCondition condition) {
        return productSummaryRepository.findAll(condition);
    }
}
