package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.utils.ChangeListener;

/**
 * The main menu screen for the game. Allows the player to start a new game, or quit to desktop.
 */
public class MainMenu implements Screen {
    /**
     * The {@code scene2d.ui} stage used to render this screen.
     */
    Stage stage;

    public MainMenu(HeslingtonHustle game) {
        var camera = new OrthographicCamera();
        var viewport = new FitViewport(GameConstants.WORLD_WIDTH * 10, GameConstants.WORLD_HEIGHT * 10, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        var root = new Table(game.skin);
        root.setFillParent(true);
        root.pad(0.25f);

        root.setDebug(game.debug);
        stage.addActor(root);

        root.add("Heslington Hustle").getActor().setFontScale(2);
        root.row();

        var inner = new Table(game.skin);

        var startButton = new TextButton("Start Game", game.skin);
        startButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.OPTIONS)));
        inner.add(startButton).pad(10).width(Value.percentWidth(0.4f, inner)).height(Value.percentHeight(0.1f, inner));

        inner.row();

        /**
         *         var optionsButton = new TextButton("Options", game.skin);
         *         optionsButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.OPTIONS)));
         *         inner.add(optionsButton).pad(10).width(Value.percentWidth(0.4f, inner)).height(Value.percentHeight(0.1f, inner));
         *
         *         inner.row();
         */
        var quitButton = new TextButton("Quit", game.skin);
        quitButton.addListener(ChangeListener.of((e, a) -> game.quit()));
        inner.add(quitButton).pad(10).width(Value.percentWidth(0.4f, inner)).height(Value.percentHeight(0.1f, inner));

        root.add(inner).grow();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
