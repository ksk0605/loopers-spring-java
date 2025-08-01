package com.loopers.domain.like;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final LikeSummaryRepository likeSummaryRepository;

    @Transactional
    public void like(Long userId, Long targetId, LikeTargetType targetType) {
        if (likeRepository.exists(userId, new LikeTarget(targetId, targetType))) {
            return;
        }
        Like like = new Like(userId, targetId, targetType);
        likeRepository.save(like);

        likeSummaryRepository.findByTarget(new LikeTarget(targetId, targetType))
            .ifPresentOrElse(
                LikeSummary::incrementLikeCount,
                () -> {
                    LikeSummary likeSummary = new LikeSummary(targetId, targetType);
                    likeSummary.incrementLikeCount();
                    likeSummaryRepository.save(likeSummary);
                });
    }

    @Transactional
    public void unlike(Long userId, Long targetId, LikeTargetType targetType) {
        if (!likeRepository.exists(userId, new LikeTarget(targetId, targetType))) {
            return;
        }
        likeRepository.delete(userId, new LikeTarget(targetId, targetType));

        likeSummaryRepository.findByTarget(new LikeTarget(targetId, targetType))
            .ifPresentOrElse(
                LikeSummary::decrementLikeCount,
                () -> {
                    LikeSummary likeSummary = new LikeSummary(targetId, targetType);
                    likeSummary.decrementLikeCount();
                    likeSummaryRepository.save(likeSummary);
                });
    }

    @Transactional(readOnly = true)
    public Long count(Long targetId, LikeTargetType targetType) {
        return likeRepository.count(new LikeTarget(targetId, targetType));
    }

    @Transactional(readOnly = true)
    public List<LikeInfo> getAll(Long userId, LikeTargetType targetType) {
        return likeRepository.findAll(userId, targetType)
            .stream()
            .map(LikeInfo::from)
            .toList();
    }
}
