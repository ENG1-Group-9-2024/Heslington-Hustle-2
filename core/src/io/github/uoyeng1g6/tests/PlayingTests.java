package io.github.uoyeng1g6.tests;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.Playing;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GdxTestRunner.class)
public class PlayingTests {
    private static boolean initialized = false;

    @BeforeClass
    public static void init() {
        if (!initialized) {
            // Initialising LibGDX in headless mode
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, config);

            initialized = true;
        }
    }

    @Mock
    private HeslingtonHustle mockGame;

    @Mock
    private OrthographicCamera mockCamera;

    @Mock
    private GameState gameState;

    private Playing playing;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        gameState = new GameState();
        mockCamera = mock(OrthographicCamera.class);

        playing = new Playing(mockGame, gameState, true);
    }

    @Test
    public void T_7DaysOver() {
        gameState.setDaysRemaining(0);
        playing.render(0);
        verify(mockGame).setState(HeslingtonHustle.State.END_SCREEN);
    }

    @Test
    public void T_7DaysNotOver() {
        gameState.setDaysRemaining(1);
        playing.render(0);
        verify(mockGame, never()).setState(HeslingtonHustle.State.END_SCREEN);
    }
}
