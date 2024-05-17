package io.github.uoyeng1g6.tests;

import static org.junit.Assert.*;

import io.github.uoyeng1g6.HeslingtonHustle;
import io.github.uoyeng1g6.constants.ActivitySubType;
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
        boolean result = gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
    }

    @Test
    public void T_16HoursInDay2() {
        // Testing when there is not enough time
        gameState.setHoursRemaining(0);
        boolean result = gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
        assertFalse("Activity should not be performed when there is not enough time", result);
    }

    @Test
    public void T_16HoursInDay3() {
        // Testing when there is not enough energy
        gameState.setEnergyRemaining(5);
        boolean result = gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
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

        boolean result = gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is exactly enough time and energy", result);

        assertEquals("No hours should remain after activity", 0, gameState.getHoursRemaining());
        assertEquals("No energy should remain after activity", 0, gameState.getEnergyRemaining());
    }

    @Test
    public void T_EnergyIsUsed() {
        gameState.setHoursRemaining(8);
        gameState.setEnergyRemaining(100);
        boolean result = gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
        assertEquals(
                "Hours remaining should decrease by the time used for the activity", 6, gameState.getHoursRemaining());
        assertEquals("Energy should decrease by the energy used for the activity", 70, gameState.getEnergyRemaining());
    }

    @Test
    public void T_activityInDayIncreased() {
        gameState.doActivity(ActivitySubType.STUDY1, "Studying...");
        assertEquals(
                "The activityStats of the activity type should be incremented",
                1,
                gameState.currentDay.activityStats.get(ActivitySubType.STUDY1).intValue());
    }

    @Test
    public void T_dayAddedToArrayList() {
        int initialSize = gameState.days.size();
        gameState.advanceDay();
        assertEquals("The day should be added to the array list", initialSize + 1, gameState.days.size());
    }


    public int bonusTestHelper(ActivitySubType type) {

        gameState = new GameState();

        gameState.setDaysRemaining(7);
        gameState.setEnergyRemaining(GameConstants.MAX_ENERGY);
        gameState.setHoursRemaining(GameConstants.MAX_HOURS);

        for(int i = 0; i < 7; i++) {
            gameState.doActivity(type, "");
            gameState.advanceDay();
        }

        HeslingtonHustle game = new HeslingtonHustle();
        EndScreen screen = new EndScreen(game, gameState, true);
        screen.calculateExamScore(gameState.days);
        return screen.bonus;
    }

    @Test
    public void T_Study1BonusTest(){
        ActivitySubType type = ActivitySubType.STUDY1;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Study2BonusTest(){
        ActivitySubType type = ActivitySubType.STUDY2;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Meal1BonusTest(){
        ActivitySubType type = ActivitySubType.MEAL1;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Meal2BonusTest(){
        ActivitySubType type = ActivitySubType.MEAL2;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Meal3BonusTest(){
        ActivitySubType type = ActivitySubType.MEAL3;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec1BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION1;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec2BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION2;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec3BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION3;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec4BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION4;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec5BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION5;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }

    @Test
    public void T_Rec6BonusTest(){
        ActivitySubType type = ActivitySubType.RECREATION6;
        int expected = GameConstants.getActivityBonus(type);
        int actual = bonusTestHelper(type);

        assertEquals("The bonus is correct", expected, actual);
    }


}
