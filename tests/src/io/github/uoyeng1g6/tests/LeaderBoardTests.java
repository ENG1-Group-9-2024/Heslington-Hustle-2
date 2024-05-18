package io.github.uoyeng1g6.tests;

import io.github.uoyeng1g6.utils.LeaderboardManager;
import io.github.uoyeng1g6.utils.PlayerScore;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class LeaderBoardTests {

    @Test
    public void testScoreRecording() {
        LeaderboardManager lm = LeaderboardManager.getInstance();
        PlayerScore score = new PlayerScore("testPlayer", 10d);
        lm.addScore(score);
        List<PlayerScore> scores = lm.getTopTenScores();
        assertTrue(scores.contains(score));
    }

    @Test
    public void testHighestScore() {
        LeaderboardManager lm = LeaderboardManager.getInstance();
        for (int i=0; i<10; i++) {
            lm.addScore("P" + i+1, 50-i);
        }
        PlayerScore p11 = new PlayerScore("P11", 100);
        lm.addScore(p11);
        List<PlayerScore> top10 = lm.getTopTenScores();
        assertEquals(top10.get(0), p11);
    }

    @Test
    public void testLowScore() {
        LeaderboardManager lm = LeaderboardManager.getInstance();
        for (int i=0; i<10; i++) {
            lm.addScore("P" + i+1, 50-i);
        }
        PlayerScore p11 = new PlayerScore("P11", 0);
        lm.addScore(p11);
        List<PlayerScore> top10 = lm.getTopTenScores();
        assertFalse(top10.contains(p11));
    }

    @Test
    public void test10Scores() {
        LeaderboardManager lm = LeaderboardManager.getInstance();
        for (int i=0; i<10; i++) {
            lm.addScore("P" + i+1, 50-i);
        }
        PlayerScore p11 = new PlayerScore("P11", 70);
        lm.addScore(p11);
        List<PlayerScore> top10 = lm.getTopTenScores();
        assertEquals(10, top10.size());
    }

    @BeforeClass
    public static void setUp() {
        LeaderboardManager.clearScores();
    }

    @After
    public void cleanUp() {
        LeaderboardManager.clearScores();
    }

}
