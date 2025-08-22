package com.loopers.application.point;

import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointHistoryService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class PointFacade {
    private final PointHistoryService pointHistoryService;
    private final UserService userService;

    public PointResult getMyPoint(String userId) {
        User user = userService.get(userId);
        return new PointResult(user.getPoint());
    }

    @Transactional
    public PointResult chargePoint(String userId, int amount) {
        User user = userService.get(userId);
        PointHistory pointHistory = pointHistoryService.earn(user.getUserId(), amount);
        user = userService.chargePoint(userId, pointHistory.getBalance());
        return new PointResult(user.getPoint());
    }
}
