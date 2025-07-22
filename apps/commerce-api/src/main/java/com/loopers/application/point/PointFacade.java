package com.loopers.application.point;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointHistoryService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Component
public class PointFacade {

    private final PointHistoryService pointHistoryService;
    private final UserService userService;

    public PointFacade(PointHistoryService pointHistoryService, UserService userService) {
        this.pointHistoryService = pointHistoryService;
        this.userService = userService;
    }

    public PointInfo getMyPoint(String userId) {
        User user = userService.findUser(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        return PointInfo.from(user.getPoint());
    }

    @Transactional
    public PointInfo chargePoint(String userId, int amount) {
        User user = userService.findUser(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 ID의 유저가 존재하지 않습니다. [userId = " + userId + "]"));
        PointHistory pointHistory = pointHistoryService.earn(user.getUserId(), amount);
        user.updatePoint(pointHistory.getBalance());
        return PointInfo.from(user.getPoint());
    }
}
