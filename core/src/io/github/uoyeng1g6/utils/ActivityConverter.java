package io.github.uoyeng1g6.utils;

import static java.util.Map.entry;

import io.github.uoyeng1g6.constants.ActivitySubType;
import io.github.uoyeng1g6.constants.ActivityType;
import java.util.Map;

public final class ActivityConverter {

    private static final Map<ActivitySubType, ActivityType> subTypeToType = Map.ofEntries(
            entry(ActivitySubType.STUDY1, ActivityType.STUDY),
            entry(ActivitySubType.STUDY2, ActivityType.STUDY),
            entry(ActivitySubType.MEAL1, ActivityType.MEAL),
            entry(ActivitySubType.MEAL2, ActivityType.MEAL),
            entry(ActivitySubType.MEAL3, ActivityType.MEAL),
            entry(ActivitySubType.RECREATION1, ActivityType.RECREATION),
            entry(ActivitySubType.RECREATION2, ActivityType.RECREATION),
            entry(ActivitySubType.RECREATION3, ActivityType.RECREATION),
            entry(ActivitySubType.RECREATION4, ActivityType.RECREATION),
            entry(ActivitySubType.RECREATION5, ActivityType.RECREATION),
            entry(ActivitySubType.RECREATION6, ActivityType.RECREATION));

    public static ActivityType convertActivity(ActivitySubType activitySubType) {
        return subTypeToType.get(activitySubType);
    }
}
