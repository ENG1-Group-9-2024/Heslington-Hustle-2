package io.github.uoyeng1g6.constants;

import java.util.Map;

import static java.util.Map.entry;

/**
 * Constants used throughout the game
 */
public final class GameConstants {
    /**
     * The width of the game world in tiles.
     */
    public static final int WORLD_WIDTH = 180;
    /**
     * The height of the game world in tiles.
     */
    public static final int WORLD_HEIGHT = 110;
    /**
     * The maximum amount of energy available to the player on a single day.
     */
    public static final int MAX_ENERGY = 100;
    /**
     * The maximum amount of hours available to the player on a single day.
     */
    public static final int MAX_HOURS = 16;
    /**
     * The number of seconds to display the interaction overlay for per hour used by the interaction.
     */
    public static final int OVERLAY_SECONDS_PER_HOUR = 2;


    private static final Map<ActivitySubType, Integer> activityTimes = Map.ofEntries(
            entry(ActivitySubType.STUDY1, 1),
            entry(ActivitySubType.STUDY2, 3),
            entry(ActivitySubType.MEAL1, 1),
            entry(ActivitySubType.MEAL2, 2),
            entry(ActivitySubType.MEAL3, 3),
            entry(ActivitySubType.RECREATION1, 1),
            entry( ActivitySubType.RECREATION2, 1),
            entry(ActivitySubType.RECREATION3, 4),
            entry(ActivitySubType.RECREATION4, 2),
            entry( ActivitySubType.RECREATION5, 4),
            entry(ActivitySubType.RECREATION6, 1)
    );

    private static final Map<ActivitySubType, Integer> activityEnergies = Map.ofEntries(
            entry(ActivitySubType.STUDY1, 10),
            entry(ActivitySubType.STUDY2, 20),
            entry(ActivitySubType.MEAL1, 5),
            entry(ActivitySubType.MEAL2, 10),
            entry(ActivitySubType.MEAL3, 10),
            entry(ActivitySubType.RECREATION1, 10),
            entry( ActivitySubType.RECREATION2, 15),
            entry(ActivitySubType.RECREATION3, 20),
            entry(ActivitySubType.RECREATION4, 30),
            entry( ActivitySubType.RECREATION5, 20),
            entry(ActivitySubType.RECREATION6, 10)
    );

    private static final Map<ActivitySubType, Integer> activityBonuses = Map.ofEntries(
            entry(ActivitySubType.STUDY1, 5),
            entry(ActivitySubType.STUDY2, 5),
            entry(ActivitySubType.MEAL1, 5),
            entry(ActivitySubType.MEAL2, 5),
            entry(ActivitySubType.MEAL3, 5),
            entry(ActivitySubType.RECREATION1, 5),
            entry( ActivitySubType.RECREATION2, 5),
            entry(ActivitySubType.RECREATION3, 5),
            entry(ActivitySubType.RECREATION4, 5),
            entry( ActivitySubType.RECREATION5, 5),
            entry(ActivitySubType.RECREATION6, 5)
    );

    public static int getActivityTime(ActivitySubType activity) {
        return activityTimes.get(activity);
    }

    public static int getActivityEnergy(ActivitySubType activity) {
        return activityEnergies.get(activity);
    }

    public static int getActivityBonus(ActivitySubType activity) {
        return activityBonuses.get(activity);
    }

}
