package io.github.uoyeng1g6.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import io.github.uoyeng1g6.HeslingtonHustle;


import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.logging.FileHandler;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



@RunWith(GdxTestRunner.class)
public class AssetTests {

    @Test
    public void t_InteractionIconExists() {

        assertNotNull("The asset for interactions exists", Gdx.files.internal(HeslingtonHustle.interactionAsset));
    }

    @Test
    public void t_PlayerIconExists() {

        assertNotNull("The asset for player exists", Gdx.files.internal(HeslingtonHustle.playerAsset));
    }

    @Test
    public void t_UISkinsExists() {

        assertNotNull("The asset for UIskin exists", Gdx.files.internal(HeslingtonHustle.UISkinAsset));
    }

    @Test
    public void t_MapAssetExists() {

        assertNotNull("The asset for map exists", Gdx.files.internal(HeslingtonHustle.mapAsset));
    }
}
