package com.loopers.application.usersignal

import com.loopers.domain.commerceevent.CommerceEventService
import com.loopers.domain.common.InternalMessage
import com.loopers.domain.usersignal.TargetType
import com.loopers.domain.usersignal.UserSignalEvent
import com.loopers.domain.usersignal.UserSignalEventType
import com.loopers.domain.usersignal.UserSignalUpdaterRegistry
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class UserSignalFacadeTest {

    @Mock
    private lateinit var commerceEventService: CommerceEventService

    @Mock
    private lateinit var userSignalUpdaterRegistry: UserSignalUpdaterRegistry

    @InjectMocks
    private lateinit var userSignalFacade: UserSignalFacade

    @DisplayName("이미 처리한 이벤트는 처리하지 않는다.")
    @Test
    fun processAlreadyProcessedEvent() {
        // arrange
        val event = InternalMessage(
            metadata = com.loopers.domain.common.Metadata("1", "1.0.0", "2025-09-03 18:05:15.819538"),
            payload = UserSignalEvent(UserSignalEventType.LIKE, 1L, TargetType.PRODUCT)
        )
        `when`(commerceEventService.isAlreadyProcessed("1")).thenReturn(true)

        // act
        userSignalFacade.updateUserSignal(listOf(event))

        // assert
        verify(userSignalUpdaterRegistry, never()).getUpdater(UserSignalEventType.LIKE)
    }
}
