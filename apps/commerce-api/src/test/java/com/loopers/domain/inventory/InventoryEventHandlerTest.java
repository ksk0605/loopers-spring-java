package com.loopers.domain.inventory;

import static com.loopers.domain.inventory.InventoryCommand.Deduct;
import static com.loopers.support.fixture.InventoryFixture.anInventory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.loopers.domain.order.OrderPaidEvent;

@ExtendWith(MockitoExtension.class)
class InventoryEventHandlerTest {
    private InventoryEventHandler inventoryEventHandler;

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        inventoryEventHandler = new InventoryEventHandler(inventoryService);
    }

    @DisplayName("OrderPaidEvent를 수신하면, 상품 재고를 차감 처리해야한다.")
    @Test
    void handleOrderPaid_shouldDeductInventory() {
        // arrange
        Inventory inventory = anInventory().quantity(10).build();
        when(inventoryRepository.findAll(any(Deduct.class))).thenReturn(List.of(inventory));

        // act
        inventoryEventHandler.handlePaymentSuccess(
            new OrderPaidEvent(
                1L,
                List.of(new OrderPaidEvent.Item(1L, 1L, 10))
            ));

        // assert
        assertThat(inventory.getQuantity()).isEqualTo(0);
    }
}
