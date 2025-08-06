package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointResult;

public class PointV1Dto {
    public record PointResponse(
        int point
    ) {
        public static PointResponse from(PointResult result) {
            return new PointResponse(result.point());
        }
    }

    public record ChargePointRequest(
        int amount
    ) {
    }
}
