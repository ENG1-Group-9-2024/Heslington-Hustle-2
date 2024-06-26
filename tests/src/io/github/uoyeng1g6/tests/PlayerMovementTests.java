package io.github.uoyeng1g6.tests;

import static org.junit.Assert.assertTrue;

import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.systems.PlayerMovementSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests that the player movement system works as expected
 */
@RunWith(GdxTestRunner.class)
public class PlayerMovementTests {

    GameState game = new GameState();
    PlayerMovementSystem test = new PlayerMovementSystem(game);

    @Test
    public void t_LeftMovement() {

        float initialVelocity = test.getVelocity().x;

        test.movementUpdated(true, false, false, false);

        float finalVelocity = test.getVelocity().x;

        assertTrue(initialVelocity > finalVelocity);
    }

    @Test
    public void t_RightMovement() {

        float initialVelocity = test.getVelocity().x;

        test.movementUpdated(false, true, false, false);

        float finalVelocity = test.getVelocity().x;

        assertTrue(initialVelocity < finalVelocity);
    }

    @Test
    public void t_UpMovement() {

        float initialVelocity = test.getVelocity().y;

        test.movementUpdated(false, false, true, false);

        float finalVelocity = test.getVelocity().y;

        assertTrue(initialVelocity < finalVelocity);
    }

    @Test
    public void t_DownMovement() {

        float initialVelocity = test.getVelocity().y;

        test.movementUpdated(false, false, false, true);

        float finalVelocity = test.getVelocity().y;

        assertTrue(initialVelocity > finalVelocity);
    }
}
