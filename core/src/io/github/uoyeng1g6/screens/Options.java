/**
 * The Options Class is an entirely new class added to the game to meet new requirements added in assessment 2
 */
package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.utils.ChangeListener;

/**
 * The Options screen for the game, allows to user to enter their name and choose between WASD or arrow key controls
 */
public class Options implements Screen {
    Camera camera;
    /**
     * The {@code scene2d.ui} stage used to render this screen.
     */
    Stage stage;
    /**
     * Texture atlas variable used to contain the player sprite information.
     */
    public static TextureAtlas playerTextureAtlas;
    /**
     * Textfield variable used to make a text box which the user could enter their name into
     */
    private final TextField nameTextField;
    /**
     * Textbutton variable used to create the submit button
     */
    private final TextButton submitButton;
    /**
     * Textbutton variables for each of the buttons used to switch between the different character models
     */
    private final TextButton playerModel1Button;
    private final TextButton playerModel2Button;
    private final TextButton playerModel3Button;
    private final TextButton playerModel4Button;

    /**
     * code for options screen functionality below. Buttons and text box for name
     * Submit button now takes the player to the start of the game,however it
     * won't work unless the player has typed a name into the username text box.
     */
    public Options(HeslingtonHustle game) {
        camera = new OrthographicCamera();
        var viewport = new ScreenViewport(camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        var root = new Table(game.skin);
        root.setFillParent(true);
        root.pad(0.25f);

        root.setDebug(game.debug);
        stage.addActor(root);

        root.add("Options").pad(10).getActor().setFontScale(2);
        root.row();

        var inner = new Table(game.skin);

        var WASD = new TextButton("WASD", game.skin);
        WASD.addListener(ChangeListener.of((e, a) -> PlayerComponent.setKeyboardControls(false)));
        inner.add(WASD).pad(10).width(Value.percentWidth(0.15f, inner)).height(Value.percentHeight(0.15f, inner));

        var arrowKeys = new TextButton("Arrow keys", game.skin);
        arrowKeys.addListener(ChangeListener.of((e, a) -> System.out.println(PlayerComponent.getUserName())));
        inner.add(arrowKeys).pad(10).width(Value.percentWidth(0.15f, inner)).height(Value.percentHeight(0.15f, inner));

        root.add(inner).padBottom(150).grow();

        nameTextField = new TextField("", game.skin);
        nameTextField.setMessageText("Enter your name");

        submitButton = new TextButton("Submit", game.skin);

        Table table = new Table();
        table.setFillParent(true);
        table.padTop(160);
        table.add(nameTextField).width(200).height(40).pad(10);
        table.row();
        table.add(submitButton).width(100).height(40).pad(10);

        stage.addActor(table);

        submitButton.addListener(event -> {
            if (submitButton.isPressed()) {
                PlayerComponent.setUserName(nameTextField.getText());
                if (!PlayerComponent.getUserName().isEmpty()) {
                    System.out.println("Username entered " + PlayerComponent.getUserName());
                    game.setState(HeslingtonHustle.State.PLAYING);
                }
            }
            return true;
        });

        int modelWidth = 64;
        int modelHeight = 64;
        int xCo = 0;
        int yCo = 128;
        /*
         * loading png of player models for the options window so the player can
         * see what models they are switching between
         */
        Texture model1 = new Texture(Gdx.files.internal("sprites/player.png"));
        TextureRegion model1Region = new TextureRegion(model1, xCo, yCo, modelWidth, modelHeight);
        Image model1Image = new Image(model1Region);

        Texture model2 = new Texture(Gdx.files.internal("sprites/player2.png"));
        TextureRegion model2Region = new TextureRegion(model2, xCo, yCo, modelWidth, modelHeight);
        Image model2Image = new Image(model2Region);

        Texture model3 = new Texture(Gdx.files.internal("sprites/player3.png"));
        TextureRegion model3Region = new TextureRegion(model3, xCo, yCo, modelWidth, modelHeight);
        Image model3Image = new Image(model3Region);

        Texture model4 = new Texture(Gdx.files.internal("sprites/player4.png"));
        TextureRegion model4Region = new TextureRegion(model4, xCo, yCo, modelWidth, modelHeight);
        Image model4Image = new Image(model4Region);

        playerModel1Button = new TextButton("Model 1", game.skin);
        playerModel2Button = new TextButton("Model 2", game.skin);
        playerModel3Button = new TextButton("Model 3", game.skin);
        playerModel4Button = new TextButton("Model 4", game.skin);

        Table outer = new Table(game.skin);
        outer.setFillParent(true);
        outer.left();
        outer.row();
        outer.add(playerModel1Button).width(100).height(100).pad(10);
        outer.row();
        outer.add(playerModel2Button).width(100).height(100).pad(10);
        outer.row();
        outer.add(playerModel3Button).width(100).height(100).pad(10);
        outer.row();
        outer.add(playerModel4Button).width(100).height(100).pad(10);

        /*
        manually adding the sprite icons next to the buttons because adding them
        using libGDX table formatting was causing problems
         */

        model1Image.setPosition(110, 380);
        model1Image.setSize(100, 100);
        stage.addActor(model1Image);
        model2Image.setPosition(110, 260);
        model2Image.setSize(100, 100);
        stage.addActor(model2Image);
        model3Image.setPosition(110, 140);
        model3Image.setSize(100, 100);
        stage.addActor(model3Image);
        model4Image.setPosition(110, 20);
        model4Image.setSize(100, 100);
        stage.addActor(model4Image);

        stage.addActor(outer);

        playerModel1Button.addListener(event -> {
            if (playerModel1Button.isPressed()) {
                playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
            }
            return true;
        });
        playerModel2Button.addListener(event -> {
            if (playerModel2Button.isPressed()) {
                playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player2.txt"));
            }
            return true;
        });
        playerModel3Button.addListener(event -> {
            if (playerModel3Button.isPressed()) {
                playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player3.txt"));
            }
            return true;
        });
        playerModel4Button.addListener(event -> {
            if (playerModel4Button.isPressed()) {
                playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player4.txt"));
            }
            return true;
        });
        if (playerTextureAtlas == null) {
            playerTextureAtlas = new TextureAtlas(Gdx.files.internal("sprites/player.txt"));
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        playerTextureAtlas.dispose();
    }
}
