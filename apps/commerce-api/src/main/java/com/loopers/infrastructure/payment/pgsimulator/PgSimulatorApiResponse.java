package com.loopers.infrastructure.payment.pgsimulator;

public record PgSimulatorApiResponse<T>(Metadata meta, T data) {
    public record Metadata(Result result, String errorCode, String message) {
        public enum Result {
            SUCCESS, FAIL
        }
    }
}
