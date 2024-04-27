package io.github.uoyeng1g6.tests;

import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.models.GameState;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
//\\import static org.junit.jupiter.api.Assertions.assertTrue;


/***
 * This is an example test. Use this to work out what needs importing and
 * how to set up the testing statements
 */
public class QuickTest {

    @Test
    public void testTimeDecrease() {

        GameState gameState = new GameState();

        int OGtime = gameState.hoursRemaining;

        gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");

        int NewTime = gameState.hoursRemaining;

        assertTrue(OGtime > NewTime);

    }


}
