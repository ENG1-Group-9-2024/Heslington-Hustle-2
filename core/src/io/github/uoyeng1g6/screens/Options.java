package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.components.PlayerComponent;
import io.github.uoyeng1g6.constants.GameConstants;
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

    private final TextField nameTextField;
    /**
     * Textfield variable used to make a text box which the user could enter their name into
     */
    private final TextButton submitButton;

    /**
     * Textbutton variable used to create the submit button
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
                if (!PlayerComponent.getUserName().isEmpty()){
                    System.out.println("Username entered " + PlayerComponent.getUserName());
                    game.setState(HeslingtonHustle.State.PLAYING);
                }
            }
            return true;
        });

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


    }


    @Override
    public void show() {

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}