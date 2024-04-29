package io.github.uoyeng1g6.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import io.github.uoyeng1g6.HeslingtonHustle;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(GdxTestRunner.class)
public class AssetTests {
    @Test
    public void t_InteractionIconExists() {

        assertTrue("The asset for interactions exists", Gdx.files.internal(HeslingtonHustle.interactionAsset).exists());
    }

    @Test
    public void t_PlayerIconExists() {

        assertTrue("The asset for player exists", Gdx.files.internal(HeslingtonHustle.playerAsset).exists());
    }

    @Test
    public void t_UISkinsExists() {

        Application.ApplicationType test2 = Gdx.app.getType();

        boolean test = Gdx.files.internal(HeslingtonHustle.UISkinAsset).exists();

        assertTrue("The asset for UIskin exists", Gdx.files.internal(HeslingtonHustle.UISkinAsset).exists());
    }

    @Test
    public void t_MapAssetExists4() {

        boolean test = Gdx.files.internal(HeslingtonHustle.mapAsset).exists();

        assertTrue("The asset for map exists", Gdx.files.internal(HeslingtonHustle.mapAsset).exists());

    }
    @Test
    public void t_MapAssetExists() {

       // boolean test = Gdx.files.internal(HeslingtonHustle.mapAsset).exists();

        assertTrue("The asset for map exists", Gdx.files.internal(HeslingtonHustle.mapAsset).exists());

    }

    @Test
    public void t_MapAssetExists2() {

       // boolean test = Gdx.files.internal(HeslingtonHustle.mapAsset).exists();

        assertTrue("The asset for map exists", Gdx.files.internal(HeslingtonHustle.mapAsset).exists());

    }

    @Test
    public void t_MapAssetExists3() {

        boolean test = Gdx.files.internal(HeslingtonHustle.mapAsset).exists();

        FileHandle test2 = Gdx.files.internal(HeslingtonHustle.mapAsset);
        boolean test3 = test2.exists();


        assertTrue("The asset for map exists", test2.exists());
    }

    //terrain.json test

}
