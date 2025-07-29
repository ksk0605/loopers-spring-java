package com.loopers.domain.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

@Component
public class PaymentValidator {

    public void validate(Payment payment) {
        checkNull(payment);

        if (payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 금액은 0원 이상이어야 합니다.");
        }
    }

    private void checkNull(Payment payment) {
        if (payment.getMethod() == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 방법을 선택해주세요.");
        }

        if (payment.getStatus() == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 상태를 선택해주세요.");
        }

        if (payment.getPaymentDate() == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 일시를 선택해주세요.");
        }

        if (payment.getOrderId() == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID를 선택해주세요.");
        }
    }
}
