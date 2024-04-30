import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.Playing;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.badlogic.gdx.backends.headless.*;
import org.junit.BeforeClass;


@RunWith(GdxTestRunner.class)
public class PlayingTests {
    private static boolean initialized = false;


    @BeforeClass
    public static void init() {
        if (!initialized) {
            // LibGDXをヘッドレスモードで初期化
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {
            }, config);

            initialized = true; // 初期化完了のフラグ
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
        MockitoAnnotations.initMocks(this);

        gameState = new GameState();
        mockCamera = mock(OrthographicCamera.class);

        playing = new Playing(mockGame, gameState, true);
    }

    @Test
    public void T_7DaysOver() {
        gameState.setDaysRemaining(0); // ゲームが終了する条件を設定
        playing.render(0); // 状態チェックメソッドを呼び出す
        verify(mockGame).setState(HeslingtonHustle.State.END_SCREEN); // 状態が終了画面に切り替わったことを検証
    }

    @Test
    public void T_7DaysNotOver() {
        gameState.setDaysRemaining(1);
        playing.render(0);
        verify(mockGame, never()).setState(HeslingtonHustle.State.END_SCREEN);
    }
}