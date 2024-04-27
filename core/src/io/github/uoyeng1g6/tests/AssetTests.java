package io.github.uoyeng1g6.tests;

import com.badlogic.gdx.Gdx;
import io.github.uoyeng1g6.HeslingtonHustle;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.junit.runner.RunWith;

import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.models.GameState;

//import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void testPlayerAssetExists() {

        HeslingtonHustle testGame = new HeslingtonHustle();
        assertTrue("The asset for player exists", Gdx.files.internal("dema").exists());

    }
}
