package com.loopers.application.point;

public record PointInfo(
    int point
) {
    public static PointInfo from(int myPoint) {
        return new PointInfo(myPoint);
    }
}
