package com.adevinta.leaderboard.infrastructure.persistence;

import com.adevinta.leaderboard.domain.Leaderboard;
import com.adevinta.leaderboard.domain.Player;
import com.adevinta.leaderboard.domain.PlayerRepository;
import com.adevinta.leaderboard.domain.Ranking;
import com.adevinta.leaderboard.infrastructure.bus.command.CommandBusMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.Tuple;

import java.util.List;
import java.util.Map;

public class LeaderboardRedis implements Leaderboard {
    private static final Logger LOG = LoggerFactory.getLogger(LeaderboardRedis.class);

    private final static String REDIS_SORTED_SET_KEY = "leaderboard";

    private Jedis jedis;
    private PlayerRepository playerRepository;

    public LeaderboardRedis(
            Jedis jedis,
            PlayerRepository playerRepository) {
        this.jedis = jedis;

        this.playerRepository = playerRepository;
    }

    @Override
    public void updatePlayer(String username, int score) {
        jedis.zadd(REDIS_SORTED_SET_KEY, score, username);
    }

    @Override
    public int getPlayerScore(String username) {
        return jedis.zscore(REDIS_SORTED_SET_KEY, username).intValue();
    }

    @Override
    public Ranking getRanking() {
        List<Tuple> result = jedis.zrevrangeWithScores(REDIS_SORTED_SET_KEY, 0, -1);

        Ranking ranking = new Ranking();

        for (Tuple tuple : result) {
            String username = tuple.getElement();
            LOG.info(username);
            Player temporaryPlayer = playerRepository.find(username);
            ranking.add(Map.entry((int) tuple.getScore(), temporaryPlayer));
        }

        return ranking;
    }
}
