package io.github.uoyeng1g6.tests;

import static org.junit.Assert.*;

import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import io.github.uoyeng1g6.models.GameState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
    }

    @Test
    public void T_16HoursInDay2() {
        // Testing when there is not enough time
        gameState.setHoursRemaining(0);
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");
        assertFalse("Activity should not be performed when there is not enough time", result);
    }

    @Test
    public void T_16HoursInDay3() {
        // Testing when there is not enough energy
        gameState.setEnergyRemaining(5);
        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");
        assertFalse("Activity should not be performed when there is not enough energy", result);
    }

    @Test
    public void T_SleepToNextDay() {
        gameState.advanceDay();
        assertEquals("Day remaining should be decremented by 1", 6, gameState.getDaysRemaining());
    }

    @Test
    public void T_boundaryInputs() {

        gameState.setHoursRemaining(1);
        gameState.setEnergyRemaining(10);

        boolean result = gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");
        assertTrue("Activity should be performed when there is exactly enough time and energy", result);

        assertEquals("No hours should remain after activity", 0, gameState.getHoursRemaining());
        assertEquals("No energy should remain after activity", 0, gameState.getEnergyRemaining());
    }

    @Test
    public void T_EnergyIsUsed() {
        gameState.setHoursRemaining(8);
        gameState.setEnergyRemaining(100);
        boolean result = gameState.doActivity(2, 30, ActivityType.STUDY, "Studying...");
        assertTrue("Activity should be performed when there is enough time and energy", result);
        assertEquals(
                "Hours remaining should decrease by the time used for the activity", 6, gameState.getHoursRemaining());
        assertEquals("Energy should decrease by the energy used for the activity", 70, gameState.getEnergyRemaining());
    }

    @Test
    public void T_activityInDayIncreased() {
        gameState.doActivity(1, 10, ActivityType.STUDY, "Studying...");
        assertEquals(
                "The activityStats of the activity type should be incremented",
                1,
                gameState.currentDay.activityStats.get(ActivityType.STUDY).intValue());
    }

    @Test
    public void T_dayAddedToArrayList() {
        int initialSize = gameState.days.size();
        gameState.advanceDay();
        assertEquals("The day should be added to the array list", initialSize + 1, gameState.days.size());
    }
}
