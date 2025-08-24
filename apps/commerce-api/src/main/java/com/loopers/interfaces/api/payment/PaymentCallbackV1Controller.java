package com.loopers.interfaces.api.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.application.payment.PaymentSyncFacade;
import com.loopers.domain.payment.PaymentCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments/callback")
public class PaymentCallbackV1Controller {
    private final PaymentSyncFacade paymentSyncFacade;

    @PostMapping
    public void callbackPayment(
        @RequestBody PaymentV1Dto.PaymentCallbackRequest request
    ) {
        PaymentCommand.Sync command = request.toCommand();
        paymentSyncFacade.syncPayment(command);
    }
}
