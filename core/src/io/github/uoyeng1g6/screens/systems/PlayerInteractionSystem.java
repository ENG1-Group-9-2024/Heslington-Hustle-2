package io.github.uoyeng1g6.screens.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;

/**
 * System to handle player interaction. Checks if the player is currently standing within the hitbox
 * of an interactable and if so, triggers the interaction for that interactable.
 */
public class PlayerInteractionSystem extends EntitySystem {
    private final ComponentMapper<PlayerComponent> plm = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<InteractionComponent> im = ComponentMapper.getFor(InteractionComponent.class);
    private final ComponentMapper<HitboxComponent> hm = ComponentMapper.getFor(HitboxComponent.class);
    private final ComponentMapper<FixtureComponent> fm = ComponentMapper.getFor(FixtureComponent.class);

    /**
     * The game state.
     */
    private final GameState gameState;

    private Entity playerEntity;
    private ImmutableArray<Entity> interactables;

    public PlayerInteractionSystem(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void addedToEngine(Engine engine) {
        playerEntity = engine.getEntitiesFor(Family.all(PlayerComponent.class, FixtureComponent.class)
                        .get())
                .first();
        interactables = engine.getEntitiesFor(
                Family.all(InteractionComponent.class, HitboxComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if (gameState.interactionOverlay != null) {
            // We cannot interact while another interaction is occurring
            return;
        }

        var plc = plm.get(playerEntity);
        if (!plc.isInteracting) {
            return;
        }

        var playerFixture = fm.get(playerEntity).fixture;
        var playerHitbox = new Circle(playerFixture.getBody().getPosition(), PlayerConstants.HITBOX_RADIUS);
        for (var entity : interactables) {
            var hc = hm.get(entity);

            var intersecting = false;
            for (var hitbox : hc.rects) {
                if (Intersector.overlaps(playerHitbox, hitbox)) {
                    intersecting = true;
                    break;
                }
            }

            if (!intersecting) {
                continue;
            }

            var ic = im.get(entity);
            ic.interactable.interact(this.gameState);
        }

        plc.isInteracting = false;
    }
}
