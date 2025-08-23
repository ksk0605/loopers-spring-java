package com.loopers.domain.inventory;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.order.OrderPaidEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryService inventoryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentSuccess(OrderPaidEvent event) {
        List<InventoryCommand.Option> options = event.getItems()
            .stream()
            .map(item -> new InventoryCommand.Option(item.productId(), item.productOptionId(),
                item.quantity()))
            .toList();
        inventoryService.deduct(new InventoryCommand.Deduct(options));
    }
}
