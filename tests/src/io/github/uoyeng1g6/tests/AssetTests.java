package io.github.uoyeng1g6.tests;

import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Gdx;
import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.screens.Playing;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests that the assets needed exist.
 */
@RunWith(GdxTestRunner.class)
public class AssetTests {
    @Test
    public void t_InteractionIconExists() {

        assertTrue(
                "The asset for interactions exists",
                Gdx.files
                        .internal("../assets/" + HeslingtonHustle.interactionAsset)
                        .exists());
    }

    @Test
    public void t_PlayerIconExists() {

        assertTrue(
                "The asset for player exists",
                Gdx.files.internal("../assets/" + HeslingtonHustle.playerAsset).exists());
    }

    @Test
    public void t_UISkinsExists() {
        assertTrue(
                "The asset for UIskin exists",
                Gdx.files.internal("../assets/" + HeslingtonHustle.UISkinAsset).exists());
    }

    @Test
    public void t_MapAssetExists() {
        assertTrue(
                "The asset for map exists",
                Gdx.files.internal("../assets/" + HeslingtonHustle.mapAsset).exists());
    }

    @Test
    public void t_TerrainAssetExists() {
        assertTrue(
                "The asset for the terrain exists",
                Gdx.files.internal("../assets/" + Playing.terrainAsset).exists());
    }
}
