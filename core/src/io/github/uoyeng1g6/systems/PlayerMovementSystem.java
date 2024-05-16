package io.github.uoyeng1g6.systems;

import static io.github.uoyeng1g6.components.PlayerComponent.keyboardControls;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;

/**
 * System to process the player's inputs and set the movement velocity, as well as set a flag
 * on the player component if they are attempting to interact with something.
 */
public class PlayerMovementSystem extends EntitySystem {
    /**
     * The game state.
     */
    private final GameState gameState;

    private final ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<FixtureComponent> fm = ComponentMapper.getFor(FixtureComponent.class);
    private final ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    private final Vector2 velocity = new Vector2();

    private Entity playerEntity;

    public Vector2 getVelocity() {
        return velocity;
    }

    public PlayerMovementSystem(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(
                        Family.all(PlayerComponent.class, AnimationComponent.class, FixtureComponent.class)
                                .get())
                .first();
    }

    @Override
    public void update(float deltaTime) {

        boolean leftKey, rightKey, upKey, downKey;

        if (gameState.interactionOverlay != null) {
            // User input is disabled as an interaction is currently happening
            var fixture = fm.get(playerEntity).fixture;
            // Stop the player - prevents bug where player would continue to move if direction
            // key was held at same frame that interaction was triggered
            fixture.getBody().setLinearVelocity(0, 0);
            return;
        }

        if (keyboardControls) {
            leftKey = Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT);
            rightKey = Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT);
            upKey = Gdx.input.isKeyPressed(Input.Keys.DPAD_UP);
            downKey = Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN);
        } else {
            leftKey = Gdx.input.isKeyPressed(Input.Keys.A);
            rightKey = Gdx.input.isKeyPressed(Input.Keys.D);
            upKey = Gdx.input.isKeyPressed(Input.Keys.W);
            downKey = Gdx.input.isKeyPressed(Input.Keys.S);
        }

        movementUpdated(leftKey, rightKey, upKey, downKey);

        var fixture = fm.get(playerEntity).fixture;
        fixture.getBody().setLinearVelocity(velocity);

        var ac = am.get(playerEntity);
        if (velocity.x == 0 && velocity.y == 0) {
            ac.currentAnimation = MoveDirection.STATIONARY;
        } else if (velocity.x != 0 && velocity.y == 0) {
            ac.currentAnimation = velocity.x > 0 ? MoveDirection.RIGHT : MoveDirection.LEFT;
        } else {
            ac.currentAnimation = velocity.y > 0 ? MoveDirection.UP : MoveDirection.DOWN;
        }

        pm.get(playerEntity).isInteracting = Gdx.input.isKeyJustPressed(Input.Keys.E);
    }

    // Extracted to new method for assessment 2
    /** Updates player velocity */
    public void movementUpdated(boolean moveLeft, boolean moveRight, boolean moveUp, boolean moveDown) {

        velocity.set(0, 0);

        if (moveLeft) {
            velocity.x = -PlayerConstants.PLAYER_SPEED;
        }
        if (moveRight) {
            velocity.x = PlayerConstants.PLAYER_SPEED;
        }
        if (moveUp) {
            velocity.y = PlayerConstants.PLAYER_SPEED;
        }
        if (moveDown) {
            velocity.y = -PlayerConstants.PLAYER_SPEED;
        }

        if ((moveLeft && moveRight) || (!moveLeft && !moveRight)) {
            velocity.x = 0;
        }
        if ((moveUp && moveDown) || (!moveUp && !moveDown)) {
            velocity.y = 0;
        }
    }
}
