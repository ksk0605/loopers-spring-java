package com.loopers.application.point;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loopers.domain.point.PointHistory;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;

@Component
public class PointFacade {

    private final PointService pointService;
    private final UserService userService;

    public PointFacade(PointService pointService, UserService userService) {
        this.pointService = pointService;
        this.userService = userService;
    }

    public PointInfo getMyPoint(String userId) {
        User user = userService.getUser(userId);
        return PointInfo.from(user.getPoint());
    }

    @Transactional
    public PointInfo chargePoint(String userId, int amount) {
        User user = userService.getUser(userId);
        PointHistory pointHistory = pointService.earnHistory(user.getUserId(), user.getPoint(), amount);
        user.updatePoint(pointHistory.getBalance());
        return PointInfo.from(user.getPoint());
    }
}
