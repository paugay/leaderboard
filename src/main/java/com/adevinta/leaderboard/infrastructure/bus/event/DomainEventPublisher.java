package com.adevinta.leaderboard.infrastructure.bus.event;

import java.util.List;

public interface DomainEventPublisher {

    void register(Class<? extends DomainEvent> domainEventType, Subscriber subscriber);

    void publish(List<DomainEvent> domainEventList);
}
