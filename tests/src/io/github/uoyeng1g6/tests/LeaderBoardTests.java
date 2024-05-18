package io.github.uoyeng1g6.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.Playing;
import io.github.uoyeng1g6.utils.LeaderboardManager;
import io.github.uoyeng1g6.utils.PlayerScore;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import java.util.List;

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
}
