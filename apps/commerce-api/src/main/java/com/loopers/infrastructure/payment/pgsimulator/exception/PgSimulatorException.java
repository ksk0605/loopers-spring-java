package com.loopers.infrastructure.payment.pgsimulator.exception;

import lombok.Getter;

@Getter
public class PgSimulatorException extends RuntimeException {

    private final String errorCode;
    private final int httpStatus; 

    public PgSimulatorException(String errorCode, String message, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
