package com.lodh8.starter.shared.events.infra;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventPublisherSpringAdapterTest {

    @Mock
    private ApplicationEventPublisher springPublisher;

    @InjectMocks
    private EventPublisherSpringAdapter adapter;

    @Test
    void publishEvent_shouldDelegateToSpringPublisher() {
        // Arrange
        Object testEvent = new Object();

        // Act
        adapter.publishEvent(testEvent);

        // Assert
        verify(springPublisher, times(1)).publishEvent(testEvent);
    }
}
