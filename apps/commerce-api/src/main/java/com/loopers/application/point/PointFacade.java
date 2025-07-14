package com.loopers.application.point;

import org.springframework.stereotype.Component;

import com.loopers.domain.point.PointService;

@Component
public class PointFacade {

    private final PointService pointService;

    public PointFacade(PointService pointService) {
        this.pointService = pointService;
    }

    public PointInfo getMyPoint(String userId) {
        int myPoint = pointService.getMyPoint(userId);
        return PointInfo.from(myPoint);
    }
}
