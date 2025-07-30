package com.loopers.domain.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentValidator {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public void validate(Payment payment) {
        checkNull(payment);

        if (payment.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new CoreException(ErrorType.BAD_REQUEST, "결제 금액은 0원 이상이어야 합니다.");
        }

        Order order = orderRepository.find(payment.getOrderId())
            .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "주문을 찾을 수 없습니다."));
        if (order.isPaid()) {
            throw new CoreException(ErrorType.BAD_REQUEST, "이미 결제된 주문입니다.");
        }

        User user = userRepository.find(order.getUserId())
            .orElseThrow(() -> new CoreException(ErrorType.BAD_REQUEST, "사용자를 찾을 수 없습니다."));
        if (!payment.isAvailable(BigDecimal.valueOf(user.getPoint()))) {
            throw new CoreException(ErrorType.BAD_REQUEST, "포인트가 부족합니다.");
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
