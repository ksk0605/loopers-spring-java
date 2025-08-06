package com.loopers.interfaces.api.point;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointResult;
import com.loopers.interfaces.api.ApiResponse;

@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    private final PointFacade pointFacade;

    public PointV1Controller(PointFacade pointFacade) {
        this.pointFacade = pointFacade;
    }

    @GetMapping
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getPoint(
        @RequestHeader("X-USER-ID") String userId
    ) {
        PointResult result = pointFacade.getMyPoint(userId);
        return ApiResponse.success(PointV1Dto.PointResponse.from(result));
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> chargePoint(
        @RequestHeader("X-USER-ID") String userId,
        @RequestBody PointV1Dto.ChargePointRequest request
    ) {
        PointResult result = pointFacade.chargePoint(userId, request.amount());
        return ApiResponse.success(PointV1Dto.PointResponse.from(result));
    }
}
