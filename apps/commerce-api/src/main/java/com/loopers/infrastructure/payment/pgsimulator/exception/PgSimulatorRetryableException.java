package com.loopers.infrastructure.payment.pgsimulator.exception;

public class PgSimulatorRetryableException extends PgSimulatorException {

    public PgSimulatorRetryableException(String errorCode, String message, int httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
