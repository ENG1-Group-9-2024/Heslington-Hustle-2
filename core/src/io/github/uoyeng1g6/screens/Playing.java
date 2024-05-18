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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import io.github.uoyeng1g6.constants.*;
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
import io.github.uoyeng1g6.utils.ActivityConverter;
import io.github.uoyeng1g6.utils.LeaderboardManager;
import io.github.uoyeng1g6.utils.PlayerScore;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

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

    // Extracted to attributes for assessment 2
    public static String terrainAsset = "maps/terrainV2.json";
    final float iconSize = 3 / 64f;

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
                            state -> String.valueOf(state.currentDay.statFor(ActivitySubType.STUDY1)
                                    + state.currentDay.statFor(ActivitySubType.STUDY2)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            dayEatLabel,
                            state -> String.valueOf(state.currentDay.statFor(ActivitySubType.MEAL1)
                                    + state.currentDay.statFor(ActivitySubType.MEAL2)
                                    + state.currentDay.statFor(ActivitySubType.MEAL3)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            dayRecreationLabel,
                            state -> String.valueOf(state.currentDay.statFor(ActivitySubType.RECREATION1)
                                    + state.currentDay.statFor(ActivitySubType.RECREATION2)
                                    + state.currentDay.statFor(ActivitySubType.RECREATION3)
                                    + state.currentDay.statFor(ActivitySubType.RECREATION4)
                                    + state.currentDay.statFor(ActivitySubType.RECREATION5)
                                    + state.currentDay.statFor(ActivitySubType.RECREATION6)))));

            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalStudyLabel,
                            state -> String.valueOf(String.valueOf(state.getTotalActivityCount(ActivitySubType.STUDY1)
                                    + state.getTotalActivityCount(ActivitySubType.STUDY2))))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalEatLabel,
                            state -> String.valueOf(state.getTotalActivityCount(ActivitySubType.MEAL1)
                                    + state.getTotalActivityCount(ActivitySubType.MEAL2)
                                    + state.getTotalActivityCount(ActivitySubType.MEAL3)))));
            engine.addEntity(engine.createEntity()
                    .add(new CounterComponent(
                            totalRecreationLabel,
                            state -> String.valueOf(state.getTotalActivityCount(ActivitySubType.RECREATION1)
                                    + state.getTotalActivityCount(ActivitySubType.RECREATION2)
                                    + state.getTotalActivityCount(ActivitySubType.RECREATION3)
                                    + state.getTotalActivityCount(ActivitySubType.RECREATION4)
                                    + state.getTotalActivityCount(ActivitySubType.RECREATION5)
                                    + state.getTotalActivityCount(ActivitySubType.RECREATION6)))));

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

    /** Creates an entity for an individual interaction location.
     * Added for assessment 2. */
    private Entity initInteractionLocation(
            Engine engine, int x, int y, ActivitySubType activitySubType, String toolTip) {

        var studyIcon = game.interactionIconsTextureAtlas.findRegion("book_icon");
        var foodIcon = game.interactionIconsTextureAtlas.findRegion("food_icon");
        var popcornIcon = game.interactionIconsTextureAtlas.findRegion("popcorn_icon");

        int time = GameConstants.getActivityTime(activitySubType);
        int energy = GameConstants.getActivityEnergy(activitySubType);
        ActivityType activityType = ActivityConverter.convertActivity(activitySubType);
        TextureAtlas.AtlasRegion icon;
        String overlayText;

        switch (activityType) {
            case STUDY:
                icon = studyIcon;
                overlayText = "Studying...";
                break;
            case MEAL:
                icon = foodIcon;
                overlayText = "Eating...";
                break;
            case RECREATION:
                icon = popcornIcon;
                overlayText = "Relaxing...";
                break;
            default:
                throw new IllegalArgumentException("Unknown activity type");
        }

        return engine.createEntity()
                .add(new TextureComponent(icon, iconSize).show())
                .add(new PositionComponent(x, y))
                .add(new HitboxComponent(
                        new Rectangle(x, y, icon.getRegionWidth() * iconSize, icon.getRegionHeight() * iconSize)))
                .add(new InteractionComponent(state -> {
                    if (!state.doActivity(activitySubType, overlayText)) {
                        // Notify insufficient time/energy
                    }
                }))
                .add(new TooltipComponent(
                        game.tooltipFont, "Press [E] " + toolTip + "\nTime: -" + time + "h\nEnergy: -" + energy));
    }

    /**
     * Initialise the entities for the interaction locations on the map.
     * Extended and refactored for assessment 2.
     *
     * @param engine the engine to create the entities for.
     * @return the created entities.
     */
    Entity[] initInteractionLocations(Engine engine) {

        var study = initInteractionLocation(engine, 30, 9, ActivitySubType.STUDY1, "Study for exams");
        var study2 = initInteractionLocation(engine, 83, 43, ActivitySubType.STUDY2, "Study for exams at Piazza");

        var food = initInteractionLocation(engine, 92, 16, ActivitySubType.MEAL1, "Eat at Piazza");
        var food2 = initInteractionLocation(engine, 11, 83, ActivitySubType.MEAL2, "Eat at home");
        var food3 = initInteractionLocation(engine, 62, 69, ActivitySubType.MEAL3, "Have a picnic");

        var recreation =
                initInteractionLocation(engine, 44, 40, ActivitySubType.RECREATION1, "Watch the builders at Ron Cooke");
        var recreation2 = initInteractionLocation(engine, 63, 18, ActivitySubType.RECREATION2, "Feed the ducks");
        var recreation3 = initInteractionLocation(engine, 122, 63, ActivitySubType.RECREATION3, "Go to the pub");
        var recreation4 =
                initInteractionLocation(engine, 133, 40, ActivitySubType.RECREATION4, "Play a game of footie");
        var recreation5 =
                initInteractionLocation(engine, 102, 96, ActivitySubType.RECREATION5, "Go to town with mates");
        var recreation6 = initInteractionLocation(engine, 31, 50, ActivitySubType.RECREATION6, "Play some sports");

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

            // Write score to file
            EndScreen endScreen = new EndScreen(game, gameState, false);
            String playerName = JOptionPane.showInputDialog("Please enter your name:");
            float finalScore = endScreen.calculateExamScore(gameState.days);

            PlayerScore playerScore = new PlayerScore(playerName, finalScore);
            LeaderboardManager.getInstance().addScore(playerScore);
            LeaderboardManager.getInstance().saveScoresToFile();

            updateLeaderboardDisplay();

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

    public void updateLeaderboardDisplay() {
        stage.clear();  // 既存のUIコンポーネントをクリア
    
        var leaderBoard = new Table(game.skin);
        leaderBoard.add("Leaderboard").getActor().setFontScale(1.5f);
        leaderBoard.row();
    
        List<PlayerScore> topScores = LeaderboardManager.getInstance().getTopTenScores();
        for (PlayerScore score : topScores) {
            leaderBoard.add(score.getName() + ": " + score.getScore())
                       .padBottom(10)
                       .row();
        }
    
        leaderBoard.setFillParent(true);
        leaderBoard.pad(0.15f);
        leaderBoard.right();
        stage.addActor(leaderBoard);
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
