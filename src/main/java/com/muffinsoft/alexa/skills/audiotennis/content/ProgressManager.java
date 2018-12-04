package com.muffinsoft.alexa.skills.audiotennis.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muffinsoft.alexa.sdk.util.ContentLoader;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;
import com.muffinsoft.alexa.skills.audiotennis.models.ProgressContainer;

import java.util.Set;

public class ProgressManager {

    private ProgressContainer container;

    public ProgressManager(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.container = new ContentLoader(objectMapper).loadContent(new ProgressContainer(), path, new TypeReference<ProgressContainer>() {
        });
    }

    public ActivityType getFirstActivity() {
        return ActivityType.valueOf(container.getDefaultActivity());
    }

    public ActivityType getNextActivity(ActivityType activityType, Set<String> unlockedActivities) {
        String nextActivity = container.getNextActivity(activityType.name(), unlockedActivities);
        if (nextActivity == null) {
            return null;
        }
        else {
            return ActivityType.valueOf(nextActivity);
        }
    }

    public String getEnemyRole() {
        return container.getEnemyRole();
    }
}
