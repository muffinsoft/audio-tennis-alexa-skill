package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserProgress {

    private int wins;
    private int losses;
    private Set<String> unlockedActivities = new HashSet<>();
    private Set<String> achievements = new HashSet<>();

    public UserProgress() {
    }

    public UserProgress(ActivityType unlockedActivity) {
        if (unlockedActivity != null) {
            this.unlockedActivities.add(unlockedActivity.name());
        }
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public Set<String> getUnlockedActivities() {
        return unlockedActivities;
    }

    public void setUnlockedActivities(String[] unlockedActivities) {
        this.unlockedActivities = new HashSet<>(Arrays.asList(unlockedActivities));
    }

    public Set<String> getAchievements() {
        return achievements;
    }

    public void setAchievements(String[] achievements) {
        this.achievements = new HashSet<>(Arrays.asList(achievements));
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "UserProgress{" +
                "wins=" + wins +
                ", losses=" + losses +
                ", unlockedActivities=" + unlockedActivities +
                ", achievements=" + achievements +
                '}';
    }

    public void iterateLoseCounter() {
        this.losses += 1;
    }

    public void iterateWinCounter() {
        this.wins += 1;
    }

    public void addUnlockedActivity(ActivityType activity) {
        this.unlockedActivities.add(activity.name());
    }
}
