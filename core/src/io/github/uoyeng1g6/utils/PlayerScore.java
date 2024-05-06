package io.github.uoyeng1g6.utils;

public class PlayerScore {

    private final String name;
    private final int score;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

}
