package com.muffinsoft.alexa.skills.audiotennis.models;

import java.util.Map;
import java.util.Objects;

public class ProgressContainer {

    private Map<String, Integer> activitiesOrder;

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

    public String getNextActivity(String key) {
        Integer currentOrder = activitiesOrder.get(key);
        Integer searchedValue = currentOrder + 1;
        for (Map.Entry<String, Integer> entry : this.activitiesOrder.entrySet()) {
            if (Objects.equals(entry.getValue(), searchedValue)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
