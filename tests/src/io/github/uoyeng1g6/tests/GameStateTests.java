package io.github.uoyeng1g6.tests;

import static org.junit.Assert.*;

import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import io.github.uoyeng1g6.screens.EndScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class tests that the game state works as expected.
 */
@RunWith(GdxTestRunner.class)
public class GameStateTests {

    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);
    }

    @Test
    public void T_16HoursInDay1() {
        // Testing when there is enough time and energy
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
    }

    @Test
    public void T_16HoursInDay2() {
        // Testing when there is not enough time
        gameState.setHoursRemaining(0);
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY1, "Studying...");
        assertFalse("Activity should not be performed when there is not enough time", result);
    }

    @Test
    public void T_16HoursInDay3() {
        // Testing when there is not enough energy
        gameState.setEnergyRemaining(5);
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY1, "Studying...");
        assertFalse("Activity should not be performed when there is not enough energy", result);
    }

    @Test
    public void T_SleepToNextDay() {
        gameState.setDaysRemaining(7);
        gameState.advanceDay();
        assertEquals("Day remaining should be decremented by 1", 6, gameState.getDaysRemaining());
    }

    @Test(expected = IllegalStateException.class)
    public void T_NumDays() {
        gameState.setDaysRemaining(7);
        for (int i=7; i>0; i--) {
            gameState.advanceDay();
        }
        gameState.advanceDay();
    }

    @Test
    public void T_boundaryInputs() {

        gameState.setHoursRemaining(1);
        gameState.setEnergyRemaining(10);

        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is exactly enough time and energy", result);

        assertEquals("No hours should remain after activity", 0, gameState.getHoursRemaining());
        assertEquals("No energy should remain after activity", 0, gameState.getEnergyRemaining());
    }

    @Test
    public void T_EnergyIsUsed() {
        gameState.setHoursRemaining(8);
        gameState.setEnergyRemaining(100);
        boolean result = gameState.doActivity(2, 30, ActivityType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
        assertEquals(
                "Hours remaining should decrease by the time used for the activity", 6, gameState.getHoursRemaining());
        assertEquals("Energy should decrease by the energy used for the activity", 70, gameState.getEnergyRemaining());
    }

    @Test
    public void T_activityInDayIncreased() {
        gameState.doActivity(1, 10, ActivityType.STUDY1, "Studying...");
        assertEquals(
                "The activityStats of the activity type should be incremented",
                1,
                gameState.currentDay.activityStats.get(ActivityType.STUDY1).intValue());
    }

    @Test
    public void T_dayAddedToArrayList() {
        int initialSize = gameState.days.size();
        gameState.advanceDay();
        assertEquals("The day should be added to the array list", initialSize + 1, gameState.days.size());
    }

    @Test
    public void T_Study1BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.STUDY1, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Study2BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.STUDY2, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Meal1BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.MEAL1, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Meal2BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.MEAL2, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Meal3BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.MEAL3, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec2BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION2, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec3BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION3, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec4BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION4, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec5BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION5, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec6BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION6, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }

    @Test
    public void T_Rec1BonusTest(){

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);


        for(int i = 0; i < 7; i++) {
            gameState.doActivity(1, 1, ActivityType.RECREATION1, "Studying...");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);

        assertEquals("The bonus is correct", 5, screen.bonus);


    }
}
