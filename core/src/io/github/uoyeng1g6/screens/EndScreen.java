package io.github.uoyeng1g6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.utils.ChangeListener;
import java.util.List;

/**
 * The end screen of the game. Displays the player's score and the total number done of each activity.
 */
public class EndScreen implements Screen {
    /**
     * Theoretical maximum day score. Allows normalising to range 0-100.
     */
    private static final float MAX_DAY_SCORE = 105.125f;
    /**
     * Theoretical minimum day score. Allows normalising to range 0-100.
     */
    private static final float MIN_DAY_SCORE = 0f;

    Camera camera;
    /**
     * The {@code scene2d.ui} stage used to render this screen.
     */
    Stage stage;

    int bonus;

    public EndScreen(HeslingtonHustle game, GameState endGameState) {
        camera = new OrthographicCamera();
        var viewport = new FitViewport(GameConstants.WORLD_WIDTH * 10, GameConstants.WORLD_HEIGHT * 10, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        var root = new Table(game.skin);
        root.setFillParent(true);
        root.pad(0.25f);

        root.setDebug(game.debug);
        stage.addActor(root);

        root.add("Game Over").getActor().setFontScale(2);
        root.row();

        var inner = new Table(game.skin);

        inner.add(String.format("Exam Score: %.2f / 100", calculateExamScore(endGameState.days)))
                .padBottom(50);
        inner.row();

        // Bonus is displayed here
        inner.add("Bonus: " + bonus);
        inner.row();

        // The achievements are displayed if they were activated

        if (study2Bool) {
            inner.add("Bookworm: Studied in the Piazza every day");
            inner.row();
        }

        if (study1Bool) {
            inner.add("Overclocked CPU: Studied in the C.S Building every day");
            inner.row();
        }

        if (meal1Bool) {
            inner.add("Money Saver: Ate at home every day");
            inner.row();
        }

        if (meal2Bool) {
            inner.add("Eat out to help out: Ate in the Piazza every day");
            inner.row();
        }

        if (meal3Bool) {
            inner.add("People Watcher: Ate a picnic every day");
            inner.row();
        }

        if (recreation1Bool) {
            inner.add("Secret crush: Watched the builders every day");
            inner.row();
        }

        if (recreation2Bool) {
            inner.add("What the duck!: Fed the ducks every day");
            inner.row();
        }

        if (recreation3Bool) {
            inner.add("Cold one: Went to the pub every day");
            inner.row();
        }

        if (recreation4Bool) {
            inner.add("Unlucky: Played (and lost) at football every day");
            inner.row();
        }

        if (recreation5Bool) {
            inner.add("Escapism: Went to town every day");
            inner.row();
        }

        if (recreation6Bool) {
            inner.add("Active lifestyle: Played sports every day");
            inner.row();
        }

        inner.add("Times Studied: "
                + (endGameState.getTotalActivityCount(ActivityType.STUDY1)
                        + endGameState.getTotalActivityCount(ActivityType.STUDY2)));
        inner.row();
        inner.add("Meals Eaten: "
                + (endGameState.getTotalActivityCount(ActivityType.MEAL1)
                        + endGameState.getTotalActivityCount(ActivityType.MEAL2)
                        + endGameState.getTotalActivityCount(ActivityType.MEAL3)));
        inner.row();
        inner.add("Recreational Activities Done: "
                + (endGameState.getTotalActivityCount(ActivityType.RECREATION1)
                        + endGameState.getTotalActivityCount(ActivityType.RECREATION2)
                        + endGameState.getTotalActivityCount(ActivityType.RECREATION3)
                        + endGameState.getTotalActivityCount(ActivityType.RECREATION4)
                        + endGameState.getTotalActivityCount(ActivityType.RECREATION5)
                        + endGameState.getTotalActivityCount(ActivityType.RECREATION6)));
        inner.row();

        // create a dict for the leaderboard
        // List<List<String>> leaderBoardEntries = new ArrayList<>();
        String[][] leaderBoardEntries = {
            {"Bob", "Alice", "John", "Goon", "idk", "fhuqiui", "Me", "You", "Him", "Reese"},
            {"90", "82", "74", "63", "58", "49", "40", "28", "10", "1"}
        };

        // add the dict to the leaderboard
        var leaderBoard = new Table(game.skin);
        leaderBoard.add("Leaderboard").getActor().setFontScale(1.5f);
        leaderBoard.row();
        for (int i = 0; i < leaderBoardEntries[0].length; i++) {
            leaderBoard
                    .add(leaderBoardEntries[0][i] + ": " + leaderBoardEntries[1][i])
                    .padBottom(10)
                    .row();
        }

        // Position the leaderboard on the right side of the screen
        leaderBoard.setFillParent(true);
        leaderBoard.pad(0.15f);
        leaderBoard.right();
        stage.addActor(leaderBoard);

        var mainMenuButton = new TextButton("Main Menu", game.skin);
        mainMenuButton.addListener(ChangeListener.of((e, a) -> game.setState(HeslingtonHustle.State.MAIN_MENU)));
        inner.add(mainMenuButton)
                .padTop(50)
                .width(Value.percentWidth(0.4f, inner))
                .height(Value.percentHeight(0.1f, inner));

        root.add(inner).grow();
    }

    /**
     * Calculate the score for a given day based on the number of activities performed. The optimal score
     * is given by studying 5 times, eating 3 times, and doing a recreational activity 3 times.
     *
     * @param studyCount the number of times the player studied during the day.
     * @param mealCount the number of times the player ate during the day.
     * @param recreationCount the number of recreational activities done by the player during the day.
     * @return the computed score given the activity counts.
     */
    float getDayScore(int studyCount, int mealCount, int recreationCount) {
        var studyPoints = 0;
        for (int i = 1; i <= studyCount; i++) {
            studyPoints += i <= 5 ? 10 : -5;
        }
        studyPoints = Math.max(0, studyPoints);

        // Calculate meal multiplier
        float mealMultiplier = 1;
        for (var i = 1; i <= mealCount; i++) {
            mealMultiplier += i <= 3 ? 0.15f : -0.025f;
        }
        mealMultiplier = Math.max(1, mealMultiplier);

        // Calculate recreation multiplier
        float recreationMultiplier = 1;
        for (var i = 1; i <= recreationCount; i++) {
            recreationMultiplier += i <= 3 ? 0.15f : -0.025f;
        }
        recreationMultiplier = Math.max(1, recreationMultiplier);

        // Calculate day score
        return studyPoints * mealMultiplier * recreationMultiplier;
    }

    // booleans used to keep track on if the activity has been performed daily
    boolean study1Bool = true;
    boolean study2Bool = true;

    boolean meal1Bool = true;
    boolean meal2Bool = true;
    boolean meal3Bool = true;

    boolean recreation1Bool = true;
    boolean recreation2Bool = true;
    boolean recreation3Bool = true;
    boolean recreation4Bool = true;
    boolean recreation5Bool = true;
    boolean recreation6Bool = true;

    int studyCount;
    int mealCount;
    int recreationCount;
    /**
     * Calculate the aggregate score of all the days.
     *
     * @param days the days to calculate the score for.
     * @return the computed game score.
     */
    float calculateExamScore(List<GameState.Day> days) {
        float totalScore = 0;

        for (var day : days) {

            // Finds if the activity has been performed for this day
            int study1Count = day.statFor(ActivityType.STUDY1);
            int study2Count = day.statFor(ActivityType.STUDY2);

            int meal1Count = day.statFor(ActivityType.MEAL1);
            int meal2Count = day.statFor(ActivityType.MEAL2);
            int meal3Count = day.statFor(ActivityType.MEAL3);

            int recreation1Count = day.statFor(ActivityType.RECREATION1);
            int recreation2Count = day.statFor(ActivityType.RECREATION2);
            int recreation3Count = day.statFor(ActivityType.RECREATION3);
            int recreation4Count = day.statFor(ActivityType.RECREATION4);
            int recreation5Count = day.statFor(ActivityType.RECREATION5);
            int recreation6Count = day.statFor(ActivityType.RECREATION6);

            if (study1Count == 0) {
                study1Bool = false;
            }

            if (study2Count == 0) {
                study2Bool = false;
            }

            if (meal1Count == 0) {
                meal1Bool = false;
            }

            if (meal2Count == 0) {
                meal2Bool = false;
            }

            if (meal3Count == 0) {
                meal3Bool = false;
            }

            if (recreation1Count == 0) {
                recreation1Bool = false;
            }

            if (recreation2Count == 0) {
                recreation2Bool = false;
            }

            if (recreation3Count == 0) {
                recreation3Bool = false;
            }

            if (recreation4Count == 0) {
                recreation4Bool = false;
            }

            if (recreation5Count == 0) {
                recreation5Bool = false;
            }

            if (recreation6Count == 0) {
                recreation6Bool = false;
            }

            studyCount = study1Count + study2Count;
            mealCount = meal1Count + meal2Count + meal3Count;
            recreationCount = recreation1Count
                    + recreation2Count
                    + recreation3Count
                    + recreation4Count
                    + recreation5Count
                    + recreation6Count;

            var dayScore = getDayScore(studyCount, mealCount, recreationCount);
            // Normalise day score between 0 and 100, round up to nearest whole number
            var normalisedDayScore = Math.ceil(((dayScore - MIN_DAY_SCORE) * 100) / (MAX_DAY_SCORE - MIN_DAY_SCORE));

            // Increase total score
            totalScore += (float) (normalisedDayScore * (1 / 7f));
        }

        // calculates bonus
        if (study1Bool) {
            bonus = bonus + 5;
        }

        if (study2Bool) {
            bonus = bonus + 5;
        }

        if (meal1Bool) {
            bonus = bonus + 5;
        }

        if (meal2Bool) {
            bonus = bonus + 5;
        }

        if (meal3Bool) {
            bonus = bonus + 5;
        }

        if (recreation1Bool) {
            bonus = bonus + 5;
        }

        if (recreation2Bool) {
            bonus = bonus + 5;
        }

        if (recreation3Bool) {
            bonus = bonus + 5;
        }

        if (recreation4Bool) {
            bonus = bonus + 5;
        }

        if (recreation5Bool) {
            bonus = bonus + 5;
        }

        if (recreation6Bool) {
            bonus = bonus + 5;
        }

        // Clamp total score from 0-100, adds bonus
        return Math.min(100, Math.max(0, (totalScore + bonus)));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }
}
