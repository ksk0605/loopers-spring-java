package com.loopers.infrastructure.like;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeSummaryRepository;
import com.loopers.domain.like.LikeTarget;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeSummaryRepositoryImpl implements LikeSummaryRepository {
    private final LikeSummaryJpaRepository likeSummaryJpaRepository;

    @Override
    public Optional<LikeSummary> findByTarget(LikeTarget target) {
        return likeSummaryJpaRepository.findByTargetForUpdate(target);
    }

    @Override
    public LikeSummary save(LikeSummary likeSummary) {
        return likeSummaryJpaRepository.save(likeSummary);
    }
}
