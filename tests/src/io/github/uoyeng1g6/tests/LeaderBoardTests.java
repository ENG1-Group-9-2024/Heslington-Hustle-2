package io.github.uoyeng1g6.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.Playing;
import io.github.uoyeng1g6.utils.LeaderboardManager;
import io.github.uoyeng1g6.utils.PlayerScore;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

@RunWith(GdxTestRunner.class)
public class LeaderBoardTests {
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
    public OrthographicCamera mockCamera;

    @Mock
    private GameState gameState;

    private Playing playing;

    private LeaderboardManager leaderboardManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        gameState = new GameState();
        leaderboardManager = mock(LeaderboardManager.class);

        playing = new Playing(mockGame, gameState, true);
    }


    // @Test
    // public void testRenderEndsGameAndSavesScore() {

    //     when(gameState.getDaysRemaining()).thenReturn(0);
    //     when(gameState.getInteractionOverlay()).thenReturn(null);

    //     doNothing().when(leaderboardManager).addScore(any(PlayerScore.class));
    //     doNothing().when(leaderboardManager).saveScoresToFile();

    //     playing.render(0);

    //     verify(gameState, times(1)).setState(HeslingtonHustle.State.END_SCREEN);
    //     verify(leaderboardManager, times(1)).addScore(any(PlayerScore.class));
    //     verify(leaderboardManager, times(1)).saveScoresToFile();
    // }

    @Test
    public void testScoreRecording() {
        when(gameState.getDaysRemaining()).thenReturn(0);
        when(gameState.getInteractionOverlay()).thenReturn(null);

        playing.render(0.1f);

        verify(leaderboardManager).addScore(any(PlayerScore.class));
        verify(leaderboardManager).saveScoresToFile();
    }
}
