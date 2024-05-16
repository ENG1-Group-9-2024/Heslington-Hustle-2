package io.github.uoyeng1g6.systems;

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
import io.github.uoyeng1g6.screens.Options;

import static com.badlogic.gdx.Input.Keys.DPAD_LEFT;
import static io.github.uoyeng1g6.components.PlayerComponent.getUserName;
import static io.github.uoyeng1g6.components.PlayerComponent.keyboardControls;

/**
 * System to process the player's inputs and set the movement velocity, as well as set a flag
 * on the player component if they are attempting to interact with something.
 */
public class PlayerInputSystem extends EntitySystem {
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

    public PlayerInputSystem(GameState gameState) {
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
        /**
         * if (keyboardControls) {
         *             moveLeft = DPAD_LEFT;
         *             moveRight = DPAD_RIGHT;
         *             moveUp = DPAD_UP;
         *             moveDown = DPAD_DOWN;
         *             interact = E;
         *         } else {
         *             moveLeft = A;
         *             moveRight = D;
         *             moveUp = W;
         *             moveDown = S;
         *             interact = J;
         *         }
         *
         *         current method is bad because of duplicated code
         *         wanted to do something like what is above but not sure how
         */
        if (keyboardControls) {
            boolean leftKey, rightKey, upKey, downKey;
            leftKey = rightKey = upKey = downKey = false;

            if (gameState.interactionOverlay != null) {
                // User input is disabled as an interaction is currently happening
                var fixture = fm.get(playerEntity).fixture;
                // Stop the player - prevents bug where player would continue to move if direction
                // key was held at same frame that interaction was triggered
                fixture.getBody().setLinearVelocity(0, 0);
                return;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) {

                leftKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
                rightKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) {

                upKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) {

                downKey = true;
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
        } else {
            boolean leftKey, rightKey, upKey, downKey;
            leftKey = rightKey = upKey = downKey = false;

            if (gameState.interactionOverlay != null) {
                // User input is disabled as an interaction is currently happening
                var fixture = fm.get(playerEntity).fixture;
                // Stop the player - prevents bug where player would continue to move if direction
                // key was held at same frame that interaction was triggered
                fixture.getBody().setLinearVelocity(0, 0);
                return;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {

                leftKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {

                rightKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {

                upKey = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {

                downKey = true;
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
    }

    public void movementUpdated(boolean leftKey, boolean rightKey, boolean upKey, boolean downKey) {

        velocity.set(0, 0);

        boolean moveLeft, moveRight, moveUp, moveDown;
        moveLeft = moveRight = moveUp = moveDown = false;

        if (leftKey) {
            velocity.x = -PlayerConstants.PLAYER_SPEED;
            moveLeft = true;
        }
        if (rightKey) {
            velocity.x = PlayerConstants.PLAYER_SPEED;
            moveRight = true;
        }
        if (upKey) {
            velocity.y = PlayerConstants.PLAYER_SPEED;
            moveUp = true;
        }
        if (downKey) {
            velocity.y = -PlayerConstants.PLAYER_SPEED;
            moveDown = true;
        }

        if ((moveLeft && moveRight) || (!moveLeft && !moveRight)) {
            velocity.x = 0;
        }
        if ((moveUp && moveDown) || (!moveUp && !moveDown)) {
            velocity.y = 0;
        }
    }
}
