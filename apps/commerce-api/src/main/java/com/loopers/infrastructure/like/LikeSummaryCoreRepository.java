package com.loopers.infrastructure.like;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.loopers.domain.like.LikeSummary;
import com.loopers.domain.like.LikeSummaryRepository;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.TargetLikeCount;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeSummaryCoreRepository implements LikeSummaryRepository {
    private final LikeSummaryJpaRepository likeSummaryJpaRepository;

    @Override
    public Optional<LikeSummary> findByTarget(LikeTarget target) {
        return likeSummaryJpaRepository.findByTargetForUpdate(target);
    }

    @Override
    public LikeSummary save(LikeSummary likeSummary) {
        return likeSummaryJpaRepository.save(likeSummary);
    }

    @Override
    public List<TargetLikeCount> findAllByTargetIn(List<LikeTarget> targets) {
        return likeSummaryJpaRepository.findAllByTargetIn(targets);
    }
}
