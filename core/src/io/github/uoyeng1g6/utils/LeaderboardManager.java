package io.github.uoyeng1g6.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Added for assessment 2
/**
 * This class holds the information about the leaderboard and the
 * methods that control its content
 */
public class LeaderboardManager {

    private static LeaderboardManager instance;

    private final String FILEPATH = "leaderboard.csv";
    private List<PlayerScore> scores;

    public LeaderboardManager() {
        scores = new ArrayList<>();
        loadScores();
    }

    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }

    public boolean addScore(String name, int score) {
        return addScore(new PlayerScore(name, score));
    }

    /**
     *
     * @param playerScore which contains the name and score of the current player
     * @return true if successful
     */
    public boolean addScore(PlayerScore playerScore) {
        scores.add(playerScore);
        scores.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));
        if (scores.size() > 10) {
            scores = scores.subList(0, 10);
        }
        return true;
    }

    /**
     * Returns a list of player scores in descending order
     */
    public List<PlayerScore> getTopTenScores() {
        return new ArrayList<>(scores);
    }

    /**
     * Saves the current score to the leaderboard.csv file
     */
    public void saveScoresToFile() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILEPATH))) {
            for (PlayerScore score : scores) {
                out.println(score.getName() + "," + score.getScore());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the current scores from the leaderboard.csv file
     */
    private void loadScores() {
        File scoreFile = new File(FILEPATH);
        if (!scoreFile.exists()) {
            try {
                scoreFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        try (Scanner scanner = new Scanner(scoreFile)) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                try {
                    if (data.length == 2) {
                        double score = Double.parseDouble(data[1]);
                        addScore(new PlayerScore(data[0], score));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid score format in leaderboard file.");
                }
            }
        } catch (FileNotFoundException e) {
            // This exception should not occur, but just to be sure, output the log
            System.err.println("Unexpected error: Score file not found after creation.");
        }
    }
}
