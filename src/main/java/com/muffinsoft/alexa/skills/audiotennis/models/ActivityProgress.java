package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

public class ActivityProgress {

    private ActivityType currentActivity;
    private ActivityType previousActivity;

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
}
