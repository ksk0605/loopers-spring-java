package com.loopers.infrastructure.payment.pgsimulator.exception;

import lombok.Getter;

@Getter
public class PgSimulatorException extends RuntimeException {

    private final String errorCode;

    public PgSimulatorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
