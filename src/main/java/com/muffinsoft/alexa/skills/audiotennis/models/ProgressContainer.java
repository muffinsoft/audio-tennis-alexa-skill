package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProgressContainer {

    private Map<String, Integer> activitiesOrder;
    private String enemyRole;

    public String getEnemyRole() {
        return enemyRole;
    }

    public void setEnemyRole(String enemyRole) {
        this.enemyRole = enemyRole;
    }

    public Map<String, Integer> getActivitiesOrder() {
        return activitiesOrder;
    }

    public void setActivitiesOrder(Map<String, Integer> activitiesOrder) {
        this.activitiesOrder = activitiesOrder;
    }

    public String getDefaultActivity() {
        for (Map.Entry<String, Integer> entry : activitiesOrder.entrySet()) {
            if (entry.getValue() == 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getNextActivity(Set<ActivityType> unlockedActivities) {

        HashSet<String> allActivities = new HashSet<>(this.activitiesOrder.keySet());
        for(ActivityType activityType : unlockedActivities) {
            allActivities.remove(activityType.name());
        }

        if (allActivities.isEmpty()) {
            return null;
        }

        String iterActivity = null;
        Integer minValue = null;

        for (String activity : allActivities) {
            Integer value = this.activitiesOrder.get(activity);
            if (minValue == null) {
                minValue = value;
            }
            if (value <= minValue) {
                value = minValue;
                iterActivity = activity;
            }
        }
        return iterActivity;
    }
}
