package com.adevinta.leaderboard.domain.createplayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.adevinta.leaderboard.domain.Player;
import com.adevinta.leaderboard.infrastructure.bus.command.Command;
import com.adevinta.leaderboard.infrastructure.bus.command.Handler;
import com.adevinta.leaderboard.infrastructure.bus.event.DomainEventPublisher;

public class CreatePlayerCommandHandler implements Handler {
    private DomainEventPublisher domainEventPublisher;

    public CreatePlayerCommandHandler(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void handle(Command command) throws JsonProcessingException {
        // TODO fix this
        CreatePlayerCommand createPlayerCommand = (CreatePlayerCommand) command;

        Player player = Player.create(
                createPlayerCommand.getUsername(),
                createPlayerCommand.getName(),
                createPlayerCommand.getEmail());

        domainEventPublisher.publish(player.pullDomainEvents());
    }
}
