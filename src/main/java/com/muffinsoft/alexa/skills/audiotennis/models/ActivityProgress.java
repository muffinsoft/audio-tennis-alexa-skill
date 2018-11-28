package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ActivityProgress {

    private ActivityType currentActivity;
    private ActivityType previousActivity;
    private int successCounter;
    private int mistakeCounter;
    private int enemyMistakeCounter;
    private int enemySuccessCounter;
    private int currentDifficult;
    private String previousWord;
    private String requiredUserReaction;

    private int activityEnemyMistakeIterationPointer;

    private boolean updateForLevel;
    private Set<String> usedWords;

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

    public int getEnemySuccessCounter() {
        return enemySuccessCounter;
    }

    public void setEnemySuccessCounter(int enemySuccessCounter) {
        this.enemySuccessCounter = enemySuccessCounter;
    }

    public void iterateEnemyMistakeCounter() {
        this.enemyMistakeCounter += 1;
    }

    public void iterateEnemySuccessCounter() {
        this.enemySuccessCounter += 1;
    }

    public String getPreviousWord() {
        return previousWord;
    }

    public void setPreviousWord(String previousWord) {
        this.previousWord = previousWord;
    }

    public String getRequiredUserReaction() {
        return requiredUserReaction;
    }

    public void setRequiredUserReaction(String reaction) {
        this.requiredUserReaction = reaction;
    }

    public int getCurrentDifficult() {
        return currentDifficult;
    }

    public void setCurrentDifficult(int currentDifficult) {
        this.currentDifficult = currentDifficult;
    }

    public boolean isUpdateForLevel() {
        return updateForLevel;
    }

    public void setUpdateForLevel(boolean updateForLevel) {
        this.updateForLevel = updateForLevel;
    }

    public void updateWithLevelSettings(int currentDifficutlt, int startWrongPointPositionValue, int iterateWrongPointPositionEveryLevels, int addendToWrongPointPosition) {
        this.updateForLevel = true;
        this.activityEnemyMistakeIterationPointer = startWrongPointPositionValue;
    }

    public int getActivityEnemyMistakeIterationPointer() {
        return activityEnemyMistakeIterationPointer;
    }

    public void setActivityEnemyMistakeIterationPointer(int activityEnemyMistakeIterationPointer) {
        this.activityEnemyMistakeIterationPointer = activityEnemyMistakeIterationPointer;
    }

    public Set<String> getUsedWords() {
        return usedWords != null ? usedWords : Collections.emptySet();
    }

    public void setUsedWords(String[] words) {
        if (words != null) {
            this.usedWords = new HashSet<>(Arrays.asList(words));
        }
    }

    public void addUsedWord(String word) {
        if (this.usedWords == null) {
            this.usedWords = new HashSet<>();
        }
        this.usedWords.add(word);
    }
}
