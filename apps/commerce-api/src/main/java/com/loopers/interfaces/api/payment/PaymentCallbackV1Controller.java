package com.loopers.interfaces.api.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loopers.domain.payment.PaymentCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments/callback")
public class PaymentCallbackV1Controller {

    @PostMapping
    public void callbackPayment(
        @RequestBody PaymentV1Dto.PaymentCallbackRequest request
    ) {
        PaymentCommand.Callback command = request.toCommand();
        System.out.println(command.toString());
    }
}
