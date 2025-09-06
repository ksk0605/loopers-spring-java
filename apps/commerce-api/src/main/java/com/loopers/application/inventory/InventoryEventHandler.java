package com.loopers.application.inventory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.loopers.domain.inventory.InventoryCommand;
import com.loopers.domain.inventory.InventoryService;
import com.loopers.domain.order.OrderCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryService inventoryService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        inventoryService.reverse(new InventoryCommand.Reverse(event.getItems()));
    }
}
