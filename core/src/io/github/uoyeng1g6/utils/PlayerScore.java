package io.github.uoyeng1g6.utils;

public class PlayerScore {

    private final String name;
    private final float finalScore;

    public String getName() {
        return name;
    }

    public float getScore() {
        return finalScore;
    }

    public PlayerScore(String name, float finalScore) {
        this.name = name;
        this.finalScore = finalScore;
    }
}
