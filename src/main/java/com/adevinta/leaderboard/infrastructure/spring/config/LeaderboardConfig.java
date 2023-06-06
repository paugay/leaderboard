package com.adevinta.leaderboard.infrastructure.spring.config;

import com.adevinta.leaderboard.domain.Leaderboard;
import com.adevinta.leaderboard.domain.PlayerRepository;
import com.adevinta.leaderboard.domain.addgame.AddGameCommand;
import com.adevinta.leaderboard.domain.addgame.AddGameCommandHandler;
import com.adevinta.leaderboard.domain.addgame.GameAddedDomainEvent;
import com.adevinta.leaderboard.domain.addgame.GameAddedDomainEventSubscriber;
import com.adevinta.leaderboard.domain.createplayer.CreatePlayerCommand;
import com.adevinta.leaderboard.domain.createplayer.CreatePlayerCommandHandler;
import com.adevinta.leaderboard.domain.createplayer.PlayerCreatedDomainEvent;
import com.adevinta.leaderboard.domain.createplayer.PlayerCreatedDomainEventSubscriber;
import com.adevinta.leaderboard.infrastructure.bus.command.CommandBus;
import com.adevinta.leaderboard.infrastructure.bus.command.CommandBusMap;
import com.adevinta.leaderboard.infrastructure.bus.event.DomainEventPublisher;
import com.adevinta.leaderboard.infrastructure.bus.event.DomainEventPublisherMap;
import com.adevinta.leaderboard.infrastructure.persistence.EventStorePostgres;
import com.adevinta.leaderboard.infrastructure.persistence.LeaderboardRedis;
import com.adevinta.leaderboard.infrastructure.persistence.PlayerRepositoryRedis;
import com.adevinta.leaderboard.types.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;

@Configuration
public class LeaderboardConfig {

    @Autowired
    private DataSource datasource;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public CommandBus commandBus() {
        CommandBusMap commandBusMap = new CommandBusMap();
        commandBusMap.register(AddGameCommand.class, addGameCommandHandler());
        commandBusMap.register(CreatePlayerCommand.class, createPlayerCommandHandler());
        return commandBusMap;
    }

    @Bean
    public AddGameCommandHandler addGameCommandHandler() {
        return new AddGameCommandHandler(domainEventPublisher());
    }

    @Bean
    public CreatePlayerCommandHandler createPlayerCommandHandler() {
        return new CreatePlayerCommandHandler(domainEventPublisher());
    }

    @Bean
    public DomainEventPublisher domainEventPublisher() {
        DomainEventPublisherMap domainEventPublisherMap = new DomainEventPublisherMap(eventStore());
        domainEventPublisherMap.register(GameAddedDomainEvent.class, gameAddedDomainEventSubscriber());
        domainEventPublisherMap.register(PlayerCreatedDomainEvent.class, playerCreatedDomainEventSubscriber());
        return domainEventPublisherMap;
    }

    @Bean
    public PlayerCreatedDomainEventSubscriber playerCreatedDomainEventSubscriber() {
        return new PlayerCreatedDomainEventSubscriber(
                leaderboard(),
                playerRepository());
    }

    @Bean
    public GameAddedDomainEventSubscriber gameAddedDomainEventSubscriber() {
        return new GameAddedDomainEventSubscriber(leaderboard());
    }

    @Bean
    public Leaderboard leaderboard() {
        return new LeaderboardRedis(
                jedis(),
                playerRepository());
    }

    @Bean
    public PlayerRepository playerRepository() {
        return new PlayerRepositoryRedis(jedis());
    }

    @Bean
    public Jedis jedis() {
        return new Jedis(redisHost, redisPort);
    }

    @Bean
    public EventStore eventStore() {
        return new EventStorePostgres(jdbcTemplate());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(datasource);
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(datasource);
    }
}
