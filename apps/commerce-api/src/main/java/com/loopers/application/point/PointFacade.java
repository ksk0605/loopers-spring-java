package com.loopers.application.point;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointHistoryService;
import com.loopers.domain.user.UserService;

@Component
public class PointFacade {

    private final PointHistoryService pointHistoryService;
    private final UserService userService;

    public PointFacade(PointHistoryService pointHistoryService, UserService userService) {
        this.pointHistoryService = pointHistoryService;
        this.userService = userService;
    }

    public PointInfo getMyPoint(String userId) {
        var user = userService.get(userId);
        return PointInfo.from(user.point());
    }

    @Transactional
    public PointInfo chargePoint(String userId, int amount) {
        var userInfo = userService.get(userId);
        PointHistory pointHistory = pointHistoryService.earn(userInfo.userId(), amount);
        userInfo = userService.updatePoint(userId, pointHistory.getBalance());
        return PointInfo.from(userInfo.point());
    }
}
