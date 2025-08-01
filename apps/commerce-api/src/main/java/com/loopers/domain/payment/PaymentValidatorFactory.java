package com.loopers.domain.payment;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentValidatorFactory {
    private final List<PaymentValidationStrategy> validators;

    public PaymentValidationStrategy getValidator(PaymentMethod method) {
        return validators.stream()
            .filter(validator -> validator.supports(method))
            .findFirst()
            .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST,
                "지원하지 않는 결제 방법입니다: " + method));
    }
}
