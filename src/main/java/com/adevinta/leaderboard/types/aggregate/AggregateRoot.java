package com.adevinta.leaderboard.types.aggregate;


import com.adevinta.leaderboard.infrastructure.bus.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    private List<DomainEvent> domainEventList;

    public AggregateRoot() {
        domainEventList = new ArrayList<DomainEvent>();
    }

    final public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> pulledDomainEventList = cloneList(domainEventList);

        domainEventList.clear();

        return pulledDomainEventList;
    }

    final protected void raise(DomainEvent domainEvent) {
        domainEventList.add(domainEvent);
    }

    private List<DomainEvent> cloneList(List<DomainEvent> originalList) {
        List<DomainEvent> clonedList = new ArrayList<>(originalList.size());
        for (DomainEvent domainEvent : originalList) clonedList.add(domainEvent);
        return clonedList;
    }

}
