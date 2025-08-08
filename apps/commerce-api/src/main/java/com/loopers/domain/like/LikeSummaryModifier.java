package com.loopers.domain.like;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeSummaryModifier {
    private final LikeSummaryRepository likeSummaryRepository;

    public void increment(LikeTarget target) {
        likeSummaryRepository.findByTarget(target)
            .ifPresentOrElse(
                LikeSummary::incrementLikeCount,
                () -> {
                    LikeSummary likeSummary = new LikeSummary(target.getId(), target.getType());
                    likeSummary.incrementLikeCount();
                    likeSummaryRepository.save(likeSummary);
                });
    }

    public void decrement(LikeTarget target) {
        LikeSummary likeSummary = likeSummaryRepository.findByTarget(target)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "좋아요 요약 정보가 존재하지 않습니다."));
        likeSummary.decrementLikeCount();
        likeSummaryRepository.save(likeSummary);
    }
}
