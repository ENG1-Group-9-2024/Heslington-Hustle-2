package io.github.uoyeng1g6.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.AnimationComponent;
import io.github.uoyeng1g6.components.CounterComponent;
import io.github.uoyeng1g6.components.FixtureComponent;
import io.github.uoyeng1g6.components.HitboxComponent;
import io.github.uoyeng1g6.components.InteractionComponent;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.components.PositionComponent;
import io.github.uoyeng1g6.components.TextureComponent;
import io.github.uoyeng1g6.components.TooltipComponent;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.constants.MoveDirection;
import io.github.uoyeng1g6.constants.PlayerConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.models.PhysicsPolygon;
import io.github.uoyeng1g6.systems.AnimationSystem;
import io.github.uoyeng1g6.systems.CounterUpdateSystem;
import io.github.uoyeng1g6.systems.DebugSystem;
import io.github.uoyeng1g6.systems.InteractionOverlayRenderingSystem;
import io.github.uoyeng1g6.systems.MapRenderingSystem;
import io.github.uoyeng1g6.systems.PlayerInteractionSystem;
import io.github.uoyeng1g6.systems.PlayerMovementSystem;
import io.github.uoyeng1g6.systems.StaticRenderingSystem;
import io.github.uoyeng1g6.systems.TooltipRenderingSystem;
import java.util.Map;

/**
 * The gameplay screen for the game. Causes a transition to the end screen once all the in-game
 * days have been completed.
 */
public class Playing implements Screen {
    final HeslingtonHustle game;

    private OrthographicCamera camera;
    private Viewport viewport;

    // For testing
    private boolean isTestMode = false;

    /**
     * The {@code scene2d.ui} stage used to render the ui overlay for this screen.
     */
    Stage stage;

    /**
     * The engine used to handle system updating.
     */
    Engine engine;
    /**
     * The current game state;
     */
    GameState gameState;
    /**
     * The box2d world used for the physics system.
     */
    World world;

    /**
     * The box2d debug renderer used when the game is running in physics debug mode.
     */
    Box2DDebugRenderer debugRenderer = null;

    // Extracted to attribute for assessment 2
    public static String terrainAsset = "maps/terrainV2.json";

    public Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/gameMusic.mp3"));

    public Playing(HeslingtonHustle game, GameState gameState, boolean isTestMode) {
        this.game = game;
        this.gameState = gameState;
        this.isTestMode = isTestMode;

        // Test mode does not allow LibGDX graphics-related components to be loaded during unit testing. Added for
        // assessment 2
        if (!isTestMode) {

            // Camera set up
            camera = new OrthographicCamera();
            camera.setToOrtho(false, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
            viewport = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, camera);

            stage = new Stage(viewport);

            var labelStyle = new Label.LabelStyle(game.tooltipFont, Color.BLACK);

            // Sets up the UI
            var uiTop = new Table();
            uiTop.setFillParent(true);
            uiTop.setDebug(game.debug);
            stage.addActor(uiTop);
            uiTop.center().top();

            // Music added for assessment 2
            gameMusic.setLooping(true);
            gameMusic.setVolume(0.75f);
            gameMusic.play();

            // Sets up the labels for the day and the clock
            var daysLabel = new Label("Monday", labelStyle);
            daysLabel.setFontScale(0.17f);
            uiTop.add(daysLabel);
            uiTop.row();
            var timeLabel = new Label("07:00", labelStyle);
            timeLabel.setFontScale(0.17f);
            uiTop.add(timeLabel);

            var counters = new Table(game.skin);
            counters.setFillParent(true);
            counters.pad(1);
            counters.setDebug(game.debug);
            stage.addActor(counters);

            // Loads the icons used for the activities
            var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
            var eatIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
            var recreationIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");
            var studyImage = new Image(studyIcon);
            var eatImage = new Image(eatIcon);
            var recreationImage = new Image(recreationIcon);

            // The counter in the top left corner
            var todayLabel = new Label("Today:", labelStyle);
            todayLabel.setFontScale(0.08f);
            var totalLabel = new Label("Total:", labelStyle);
            totalLabel.setFontScale(0.08f);

            var dayStudyLabel = new Label("0", labelStyle);
            dayStudyLabel.setFontScale(0.15f);
            var totalStudyLabel = new Label("0", labelStyle);
            totalStudyLabel.setFontScale(0.15f);

            var dayEatLabel = new Label("0", labelStyle);
            dayEatLabel.setFontScale(0.15f);
            var totalEatLabel = new Label("0", labelStyle);
            totalEatLabel.setFontScale(0.15f);

            var dayRecreationLabel = new Label("0", labelStyle);
            dayRecreationLabel.setFontScale(0.15f);
            var totalRecreationLabel = new Label("0", labelStyle);
            totalRecreationLabel.setFontScale(0.15f);

            counters.top().right();
            counters.add();
            counters.add(todayLabel).padRight(0.5f);
            counters.add(totalLabel);
            counters.row();
            counters.add(studyImage).width(3).height(3).padRight(0.25f);
            counters.add(dayStudyLabel);
            counters.add(totalStudyLabel);
            counters.row();
            counters.add(eatImage).width(3).height(3).padRight(0.25f);
            counters.add(dayEatLabel);
            counters.add(totalEatLabel);
            counters.row();
            counters.add(recreationImage).width(3).height(3).padRight(0.25f);
            counters.add(dayRecreationLabel);
            counters.add(totalRecreationLabel);

            // the energy in top right
            var energy = new Table(game.skin);
            energy.setFillParent(true);
            energy.pad(1);
            energy.setDebug(game.debug);
            stage.addActor(energy);

            var energyLabel = new Label("Energy Remaining:", labelStyle);
            energyLabel.setFontScale(0.08f);
            var energyAmount = new Label(String.valueOf(GameConstants.MAX_ENERGY), labelStyle);
            energyAmount.setFontScale(0.15f);

            energy.top().left();
            energy.add(energyLabel);
            energy.row();
            energy.add(energyAmount);

            this.engine = new PooledEngine();
            this.world = new World(new Vector2(), true);

            // adds colliders to map
            initTerrain();

            // Create entities
            engine.addEntity(initPlayerEntity(engine));

            for (var entity : initInteractionLocations(engine)) {
                engine.addEntity(entity);
            }

            // The day names
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(daysLabel, new CounterComponent.CounterValueResolver() {
                        // spotless:off
                    private final Map<Integer, String> dayNameMap = Map.of(
                            7, "Monday", 6, "Tuesday", 5, "Wednesday",
                            4, "Thursday", 3, "Friday", 2, "Saturday",
                            1, "Sunday - Exam Tomorrow"
                    );
                    // spotless:on

                        @Override
                        public String resolveValue(GameState gameState) {
                            return dayNameMap.get(gameState.daysRemaining);
                        }
                    })));
            // The clock display
            engine.addEntity(engine.createEntity().add(new CounterComponent(timeLabel, state -> {
                var newHour = 7 + (GameConstants.MAX_HOURS - state.hoursRemaining);
                return String.format("%s%d:00", newHour < 10 ? "0" : "", newHour);
            })));

            // The counters get updated here
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            dayStudyLabel,
                            state -> String.valueOf(state.currentDay.statFor(ActivityType.STUDY1)
                                    + state.currentDay.statFor(ActivityType.STUDY2)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            dayEatLabel,
                            state -> String.valueOf(state.currentDay.statFor(ActivityType.MEAL1)
                                    + state.currentDay.statFor(ActivityType.MEAL2)
                                    + state.currentDay.statFor(ActivityType.MEAL3)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            dayRecreationLabel,
                            state -> String.valueOf(state.currentDay.statFor(ActivityType.RECREATION1)
                                    + state.currentDay.statFor(ActivityType.RECREATION2)
                                    + state.currentDay.statFor(ActivityType.RECREATION3)
                                    + state.currentDay.statFor(ActivityType.RECREATION4)
                                    + state.currentDay.statFor(ActivityType.RECREATION5)
                                    + state.currentDay.statFor(ActivityType.RECREATION6)))));

            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalStudyLabel,
                            state -> String.valueOf(String.valueOf(state.getTotalActivityCount(ActivityType.STUDY1)
                                    + state.getTotalActivityCount(ActivityType.STUDY2))))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalEatLabel,
                            state -> String.valueOf(state.getTotalActivityCount(ActivityType.MEAL1)
                                    + state.getTotalActivityCount(ActivityType.MEAL2)
                                    + state.getTotalActivityCount(ActivityType.MEAL3)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalRecreationLabel,
                            state -> String.valueOf(state.getTotalActivityCount(ActivityType.RECREATION1)
                                    + state.getTotalActivityCount(ActivityType.RECREATION2)
                                    + state.getTotalActivityCount(ActivityType.RECREATION3)
                                    + state.getTotalActivityCount(ActivityType.RECREATION4)
                                    + state.getTotalActivityCount(ActivityType.RECREATION5)
                                    + state.getTotalActivityCount(ActivityType.RECREATION6)))));

            // the energy gets updated here
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(energyAmount, state -> String.valueOf(state.energyRemaining))));

            // Initialises the game systems

            engine.addSystem(new PlayerMovementSystem(gameState));
            engine.addSystem(new PlayerInteractionSystem(gameState));
            engine.addSystem(new MapRenderingSystem(game.tiledMap, camera));
            engine.addSystem(new StaticRenderingSystem(game.spriteBatch));
            engine.addSystem(new AnimationSystem(game.spriteBatch, gameState));
            engine.addSystem(
                    new TooltipRenderingSystem(game.tooltipFont, game.shapeDrawer, game.spriteBatch, gameState));
            engine.addSystem(new CounterUpdateSystem(gameState));
            if (game.debug) {
                engine.addSystem(new DebugSystem(game.shapeDrawer));
            }
            engine.addSystem(new InteractionOverlayRenderingSystem(
                    game.spriteBatch, game.overlayFont, game.shapeDrawer, gameState));

            if (game.physicsDebug) {
                debugRenderer = new Box2DDebugRenderer();
            }
        }
    }

    public Playing(HeslingtonHustle game) {
        this(game, new GameState(), false);
    }

    /**
     * Get the current game state object.
     *
     * @return the game state for this playthrough.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Load the terrain polygons from the data json file and create them in the box2d world.
     */
    void initTerrain() {
        var json = new Json();
        var objects = json.fromJson(PhysicsPolygon[].class, Gdx.files.internal(terrainAsset));

        for (var object : objects) {
            var bodyDef = new BodyDef();
            bodyDef.type = object.getType();
            bodyDef.position.set(object.getPosition());

            var body = world.createBody(bodyDef);
            var shape = new PolygonShape();
            shape.set(object.getVertices());

            // We know that these will always be static bodies so will always have a density of 0
            body.createFixture(shape, 0f);
            shape.dispose();
        }
    }

    /**
     * Initialise the entities for the interaction locations on the map
     *
     * @param engine the engine to create the entities for.
     * @return the created entities.
     */
    Entity[] initInteractionLocations(Engine engine) {
        final var iconSize = 3 / 64f;

        // Adds the studying activity by CS
        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var study = engine.createEntity()
                .add(new TextureComponent(studyIcon, iconSize).show())
                .add(new PositionComponent(30, 9))
                .add(new HitboxComponent(new Rectangle(
                        30, 9, studyIcon.getRegionWidth() * iconSize, studyIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.STUDY1, "Studying...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Study for exams\nTime: -1h\nEnergy: -10"));

        // Adds the studying activity by the piazza
        var study2 = engine.createEntity()
                .add(new TextureComponent(studyIcon, iconSize).show())
                .add(new PositionComponent(83, 43))
                .add(new HitboxComponent(new Rectangle(
                        83, 43, studyIcon.getRegionWidth() * iconSize, studyIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(3, 20, ActivityType.STUDY2, "Studying...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(
                        game.tooltipFont, "Press [E] Study for exams at Piazza\nTime: -1h\nEnergy: -10"));

        // Adds the eating activity by Piazza
        var foodIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
        var food = engine.createEntity()
                .add(new TextureComponent(foodIcon, iconSize).show())
                .add(new PositionComponent(92, 16))
                .add(new HitboxComponent(new Rectangle(
                        92, 16, foodIcon.getRegionWidth() * iconSize, foodIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 5, ActivityType.MEAL1, "Eating...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Eat at Piazza\nTime: -1h\nEnergy: -5"));

        // Adds the eating activity picnic
        var food3 = engine.createEntity()
                .add(new TextureComponent(foodIcon, iconSize).show())
                .add(new PositionComponent(62, 69))
                .add(new HitboxComponent(new Rectangle(
                        62, 69, foodIcon.getRegionWidth() * iconSize, foodIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(3, 10, ActivityType.MEAL3, "Eating...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Have a picnic\nTime: -1h\nEnergy: -5"));

        // Adds the eating activity by accomodation
        var food2 = engine.createEntity()
                .add(new TextureComponent(foodIcon, iconSize).show())
                .add(new PositionComponent(11, 83))
                .add(new HitboxComponent(new Rectangle(
                        11, 83, foodIcon.getRegionWidth() * iconSize, foodIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(2, 10, ActivityType.MEAL2, "Eating...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Eat at home\nTime: -1h\nEnergy: -5"));

        // Adds the watching builders activity
        var popcornIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");
        var recreation = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(44, 40))
                .add(new HitboxComponent(new Rectangle(
                        44, 40, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.RECREATION1, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(
                        game.tooltipFont, "Press [E] Watch the builders at Ron Cooke\nTime: -1h\nEnergy: -10"));

        // Adds the feeding ducks activity
        var recreation2 = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(63, 18))
                .add(new HitboxComponent(new Rectangle(
                        63, 18, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 15, ActivityType.RECREATION2, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Feed the ducks\nTime: -1h\nEnergy: -10"));

        // Adds the pub activity
        var recreation3 = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(122, 63))
                .add(new HitboxComponent(new Rectangle(
                        122, 63, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(4, 20, ActivityType.RECREATION3, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Go to the pub\nTime: -1h\nEnergy: -10"));

        // Adds the football activity
        var recreation4 = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(133, 40))
                .add(new HitboxComponent(new Rectangle(
                        133, 40, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(2, 30, ActivityType.RECREATION4, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Play a game of footie\nTime: -1h\nEnergy: -10"));

        // Adds the town activity
        var recreation5 = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(102, 96))
                .add(new HitboxComponent(new Rectangle(
                        102, 96, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(4, 20, ActivityType.RECREATION5, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Go to town with mates\nTime: -1h\nEnergy: -10"));

        // Adds the sports activity
        var recreation6 = engine.createEntity()
                .add(new TextureComponent(popcornIcon, iconSize).show())
                .add(new PositionComponent(31, 50))
                .add(new HitboxComponent(new Rectangle(
                        31, 50, popcornIcon.getRegionWidth() * iconSize, popcornIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(1, 10, ActivityType.RECREATION6, "Relaxing...")) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Play some sports\nTime: -1h\nEnergy: -10"));

        // Adds the sleep activity
        var sleepIcon = game.interactionIconsTextureAtlas.findRegion("bed_icon");
        var sleep = engine.createEntity()
                .add(new TextureComponent(sleepIcon, iconSize).show())
                .add(new PositionComponent(19, 83))
                .add(new HitboxComponent(new Rectangle(
                        19, 83, sleepIcon.getRegionWidth() * iconSize, sleepIcon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(GameState::advanceDay))
                .add(new TooltipComponent(game.tooltipFont, "Press [E] Go to sleep\nEnds the current day"));

        return new Entity[] {
            study,
            study2,
            food,
            food2,
            food3,
            recreation,
            recreation2,
            recreation3,
            recreation4,
            recreation5,
            recreation6,
            sleep
        };
    }

    /**
     * Initialise the player's physics object in the box2d world.
     *
     * @return the fixture for the player's physics object.
     */
    Fixture initPlayerBody() {
        var player = new BodyDef();
        player.type = BodyDef.BodyType.DynamicBody;
        player.position.set(PlayerConstants.START_POSITION);
        var playerBody = world.createBody(player);
        playerBody.setUserData(PlayerConstants.HITBOX_RADIUS);
        var playerCircle = new CircleShape();
        playerCircle.setRadius(PlayerConstants.HITBOX_RADIUS);
        var playerFixture = playerBody.createFixture(playerCircle, 1f);
        playerCircle.dispose();

        return playerFixture;
    }

    /**
     * Initialise the entity that represents the player character.
     *
     * @param engine the engine to create the entity for.
     * @return the created entity.
     */
    Entity initPlayerEntity(Engine engine) {
        var playerAnimations = new AnimationComponent(0.05f);
        playerAnimations.animations.put(
                MoveDirection.STATIONARY,
                new Animation<>(1f, game.playerTextureAtlas.createSprites("stationary"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.UP,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_up"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.DOWN,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_down"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.LEFT,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_left"), Animation.PlayMode.LOOP));
        playerAnimations.animations.put(
                MoveDirection.RIGHT,
                new Animation<>(0.12f, game.playerTextureAtlas.createSprites("walk_right"), Animation.PlayMode.LOOP));

        return engine.createEntity()
                .add(new PlayerComponent())
                .add(playerAnimations)
                .add(new FixtureComponent(initPlayerBody()));
    }

    @Override
    public void render(float delta) {
        // Allow the final interaction (day transition) to complete before showing the end screen
        if (gameState.daysRemaining == 0 && gameState.interactionOverlay == null) {
            game.setState(HeslingtonHustle.State.END_SCREEN);
            return;
        }

        ScreenUtils.clear(0, 0, 0, 1);

        // This makes transpacency work properly
        if (!isTestMode) {
            Gdx.gl.glEnable(GL30.GL_BLEND);
            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

            game.spriteBatch.setProjectionMatrix(camera.combined);
            game.spriteBatch.begin();

            engine.update(delta);
            if (game.physicsDebug) {
                debugRenderer.render(world, camera.combined);
            }
            game.spriteBatch.end();

            stage.act();
            stage.draw();

            world.step(delta, 8, 3);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}
