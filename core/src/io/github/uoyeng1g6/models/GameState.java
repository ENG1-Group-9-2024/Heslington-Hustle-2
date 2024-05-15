package io.github.uoyeng1g6.models;

import io.github.uoyeng1g6.constants.ActivityType;
import io.github.uoyeng1g6.constants.GameConstants;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Dataclass representing the game state.
 */
public class GameState {
    /**
     * Dataclass representing a single in-game day.
     */
    public static class Day {
        /**
         * Map of activity type to number of activities completed of that type.
         */
        public final HashMap<ActivityType, Integer> activityStats = new HashMap<>();

        /**
         * Get the number of times an activity of a specific type has been done.
         *
         * @param type the type of activity.
         * @return the number of times an activity of the given type has been done.
         */
        public int statFor(ActivityType type) {
            return activityStats.getOrDefault(type, 0);
        }

        // The following indicate whether the corresponding activities have been performed
        public boolean study1Bool = true;
        public boolean study2Bool = true;

        public boolean meal1Bool = true;
        public boolean meal2Bool = true;
        public boolean meal3Bool = true;

        public boolean recreation1Bool = true;
        public boolean recreation2Bool = true;
        public boolean recreation3Bool = true;
        public boolean recreation4Bool = true;
        public boolean recreation5Bool = true;
        public boolean recreation6Bool = true;
    }

    /**
     * Dataclass representing an overlay to show while an interaction is occurring.
     */
    public static class InteractionOverlay {
        /**
         * The text to show on the overlay.
         */
        public final String text;
        /**
         * How long the overlay should be shown for, in seconds.
         */
        public final float displayFor;

        public InteractionOverlay(String text, float displayFor) {
            this.text = text;
            this.displayFor = displayFor;
        }
    }

    /**
     * The days that have already been completed.
     */
    public final ArrayList<Day> days = new ArrayList<>(7);
    /**
     * The day that is currently in progress.
     */
    public Day currentDay = new Day();

    /**
     * The number of days remaining before the game ends.
     */
    public int daysRemaining = 7;
    /**
     * The amount of energy remaining to perform activities for the current day.
     */
    public int energyRemaining = GameConstants.MAX_ENERGY;
    /**
     * The number of hours remaining to perform activities for the current day.
     */
    public int hoursRemaining = GameConstants.MAX_HOURS;

    /**
     * The currently displayed overlay.
     */
    public InteractionOverlay interactionOverlay = null;

    /**
     * End and store the current day and advance to a new one. Resets the current energy and hours remaining.
     * Shows an overlay to indicate that the player is "sleeping".
     */
    public void advanceDay() {
        daysRemaining--;
        energyRemaining = GameConstants.MAX_ENERGY;
        hoursRemaining = GameConstants.MAX_HOURS;

        days.add(currentDay);
        currentDay = new Day();

        interactionOverlay = new InteractionOverlay("Sleeping...", 5);
    }

    /**
     * Do an activity. Subtracts the amount of time and energy required to do the activity and displays
     * an overlay. If there are not enough hours left in the day, or the player does not have enough energy
     * then returns {@code false}.
     *
     * @param timeUsage   the amount of time the activity requires.
     * @param energyUsage the amount of energy the activity requires.
     * @param type        the type of activity being done.
     * @param overlayText the text to show on the overlay while doing the interaction.
     * @return boolean indicating whether the activity could be performed.
     */
    public boolean doActivity(int timeUsage, int energyUsage, ActivityType type, String overlayText) {
        if (hoursRemaining < timeUsage || energyRemaining < energyUsage) {
            return false;
        }

        hoursRemaining -= timeUsage;
        energyRemaining -= energyUsage;
        currentDay.activityStats.merge(type, 1, Integer::sum);

        interactionOverlay = new InteractionOverlay(overlayText, GameConstants.OVERLAY_SECONDS_PER_HOUR * timeUsage);

        return true;
    }

    /**
     * Get the total number of activities of the given type done over all days including the current.
     *
     * @param type the type of activity to get the total for.
     * @return the total number of activities of that type done.
     */
    public int getTotalActivityCount(ActivityType type) {
        return days.stream().mapToInt(day -> day.statFor(type)).sum() + currentDay.statFor(type);
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public int getEnergyRemaining() {
        return energyRemaining;
    }

    public int getHoursRemaining() {
        return hoursRemaining;
    }

    public void setDaysRemaining(int days) {
        this.daysRemaining = days;
    }

    public void setEnergyRemaining(int energy) {
        this.energyRemaining = energy;
    }

    public void setHoursRemaining(int hours) {
        this.hoursRemaining = hours;
    }
}
