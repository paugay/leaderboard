package com.adevinta.leaderboard.domain.addgame;

import com.adevinta.leaderboard.domain.Game;
import com.adevinta.leaderboard.infrastructure.bus.command.Command;
import com.adevinta.leaderboard.infrastructure.bus.command.Handler;
import com.adevinta.leaderboard.infrastructure.bus.event.DomainEventPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AddGameCommandHandler implements Handler {
    private DomainEventPublisher domainEventPublisher;

    public AddGameCommandHandler(DomainEventPublisher domainEventPublisher) {
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void handle(Command command) throws JsonProcessingException {
        // TODO fix this shit!
        AddGameCommand addGameCommand = (AddGameCommand) command;

        Game game = Game.create(
                addGameCommand.getId(),
                addGameCommand.getHomePlayerId(),
                addGameCommand.getHomeScore(),
                addGameCommand.getAwayPlayerId(),
                addGameCommand.getAwayScore());

        domainEventPublisher.publish(game.pullDomainEvents());
    }
}
