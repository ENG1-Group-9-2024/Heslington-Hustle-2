package io.github.uoyeng1g6.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeaderboardManager {

    private static LeaderboardManager instance;

    private final String FILEPATH = "leaderboard.csv";


    public LeaderboardManager() {

    }

    public boolean addScore(String name, int score) {
        return true;
    }

    public boolean addScore(PlayerScore playerScore) {
        return true;
    }

    /**
     * Returns a list of player scores in descending order
     */
    public List<PlayerScore> getTopTenScores() {
        return new ArrayList<PlayerScore>();
    }

}
