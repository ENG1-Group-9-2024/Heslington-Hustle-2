package io.github.uoyeng1g6.tests;

import com.badlogic.gdx.math.Vector2;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.systems.PlayerInputSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class PlayerMovementTests {

    GameState game = new GameState();
    PlayerInputSystem test = new PlayerInputSystem(game);
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
