package com.adevinta.leaderboard.domain;

public interface PlayerRepository {
    void add(Player player);

    Player find(String username);
}
