package io.github.uoyeng1g6.utils;

// Added for assessment 2
public class PlayerScore {

    private final String name;
    private final double finalScore;

    public String getName() {
        return name;
    }

    public double getScore() {
        return finalScore;
    }

    public PlayerScore(String name, double finalScore) {
        this.name = name;
        this.finalScore = finalScore;
    }
}
