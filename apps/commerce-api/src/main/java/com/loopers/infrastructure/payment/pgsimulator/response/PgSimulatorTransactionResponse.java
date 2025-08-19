package com.loopers.infrastructure.payment.pgsimulator.response;

import lombok.Getter;

@Getter
public class PgSimulatorTransactionResponse {
    private final String transactionKey;
    private final String status;
    private final String reason;

    public PgSimulatorTransactionResponse(String transactionKey, String status, String reason) {
        this.transactionKey = transactionKey;
        this.status = status;
        this.reason = reason;
    }
}
