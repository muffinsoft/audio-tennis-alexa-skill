package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

public class ActivityProgress {

    private ActivityType currentActivity;
    private ActivityType previousActivity;
    private int successCounter;
    private int mistakeCounter;
    private int enemyMistakeCounter;

    public static ActivityType getDefaultActivity() {
        return IoC.provideProgressManager().getFirstActivity();
    }

    public static ActivityProgress createDefault() {
        ActivityType activityType = IoC.provideProgressManager().getFirstActivity();
        ActivityProgress activityProgress = new ActivityProgress();
        activityProgress.setCurrentActivity(activityType);
        return activityProgress;
    }

    public ActivityType getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(ActivityType currentActivity) {
        this.currentActivity = currentActivity;
    }

    public ActivityType getPreviousActivity() {
        return previousActivity;
    }

    public void setPreviousActivity(ActivityType previousActivity) {
        this.previousActivity = previousActivity;
    }

    public int getSuccessCounter() {
        return successCounter;
    }

    public void setSuccessCounter(int successCounter) {
        this.successCounter = successCounter;
    }

    public int getMistakeCounter() {
        return mistakeCounter;
    }

    public void setMistakeCounter(int mistakeCounter) {
        this.mistakeCounter = mistakeCounter;
    }

    public int getEnemyMistakeCounter() {
        return enemyMistakeCounter;
    }

    public void setEnemyMistakeCounter(int enemyMistakeCounter) {
        this.enemyMistakeCounter = enemyMistakeCounter;
    }

    public void iterateSuccessCounter() {
        this.successCounter += 1;
    }

    public void iterateMistakeCounter() {
        this.mistakeCounter += 1;
    }

    public void iterateEnemyMistakeCounter() {
        this.enemyMistakeCounter += 1;
    }
}
