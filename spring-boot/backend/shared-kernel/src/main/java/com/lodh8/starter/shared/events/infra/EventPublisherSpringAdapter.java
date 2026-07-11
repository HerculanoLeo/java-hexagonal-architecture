package com.lodh8.starter.shared.events.infra;

import com.lodh8.starter.shared.events.app.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisherSpringAdapter implements EventPublisherPort {

    private final ApplicationEventPublisher springPublisher;

    @Override
    public void publishEvent(Object event) {
        this.springPublisher.publishEvent(event);
    }
}
