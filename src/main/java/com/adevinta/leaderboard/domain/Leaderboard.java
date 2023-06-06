package com.adevinta.leaderboard.domain;

public interface Leaderboard {
    void updatePlayer(String username, int score);

    int getPlayerScore(String username);

    Ranking getRanking();
}
