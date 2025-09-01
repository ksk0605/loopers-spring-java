package com.loopers.domain.usersignal;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.like.LikeRepository;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserSignalService {
    private final UserSignalRepository userSignalRepository;
    private final LikeRepository likeRepository;

    public void updateLikeCount(TargetType type, Long targetId) {
        UserSignal signal = userSignalRepository.find(type, targetId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        Long count = likeRepository.count(new LikeTarget(targetId, LikeTargetType.from(type.name())));
        signal.updateLikeCount(count);
    }

    public void increaseViews(TargetType type, Long targetId) {
        UserSignal signal = userSignalRepository.find(type, targetId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        signal.incrementViews();
    }

    public List<TargetLikeCount> getTargetLikeCountsIn(List<Long> productIds, TargetType targetType) {
        List<UserSignal> signals = userSignalRepository.findAllIn(productIds, targetType);
        return signals.stream()
            .map(signal -> TargetLikeCount.from(signal))
            .toList();
    }
}
