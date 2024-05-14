package io.github.uoyeng1g6.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.EndScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class ActivityStreaksTests {

    private GameState gameState;
    private EndScreen endScreen;



    @Before
    public void setUp() {
        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);

    }

    @Test
    public void t_Study1Bonus() {

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        gameState.doActivity(1, 1, ActivityType.STUDY1, "t");
        gameState.advanceDay();

        HeslingtonHustle game = new HeslingtonHustle();

        //game.setState();

        endScreen = new EndScreen(game, gameState);


        float score = game.endScreen.calculateExamScore(gameState.days);

        assertEquals("The bonus is added", 5.0, score, 0.0);
    }

}

