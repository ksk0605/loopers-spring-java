package com.loopers.domain.rank;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RankRepositoryRegistry {
    private final List<RankRepository> repositories;

    public RankRepository getRepository(Period period) {
        return repositories.stream()
            .filter(repository -> repository.supports(period))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 기간 타입입니다."));
    }
}
