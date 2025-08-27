package com.loopers.domain.usersignal;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserSignalService {
    private final UserSignalRepository userSignalRepository;

    public void increaseLikeCount(TargetType type, Long targetId) {
        UserSignal signal = userSignalRepository.find(type, targetId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        signal.incrementLikeCount();
    }

    public void decreaseLikeCount(TargetType type, Long targetId) {
        UserSignal signal = userSignalRepository.find(type, targetId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        signal.decreaseLikeCount();
    }

    public void increaseViews(TargetType type, Long targetId) {
        UserSignal signal = userSignalRepository.find(type, targetId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
        signal.incrementViews();
    }
}
