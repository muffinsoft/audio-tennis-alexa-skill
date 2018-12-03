package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.skills.audiotennis.IoC;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ActivityProgress {

    private ActivityType currentActivity;
    private ActivityType previousActivity;
    private Set<ActivityType> unlockedActivities = new HashSet<>();
    private int successCounter;
    private int mistakeCount;
    private int enemyAnswerCounter;
    private int enemyPointCounter;
    private int playerPointCounter;
    private int playerGameCounter;
    private int enemyGameCounter;
    private int playerGameWinInRow;
    private int enemyGameWinInRow;
    private int amountOfGameInRow;
    private String previousWord;
    private String requiredUserReaction;

    private int complexity;

    private boolean updateForLevel;
    private Set<String> usedWords = new HashSet<>();
    private ActivityType possibleActivity;

    private ActivityProgress() {
    }

    public ActivityProgress(ActivityType currentActivity) {
        this.currentActivity = currentActivity;
        this.unlockedActivities.add(currentActivity);
    }

    @JsonIgnore
    public static ActivityType getDefaultActivity() {
        return IoC.provideProgressManager().getFirstActivity();
    }

    @JsonIgnore
    public static ActivityProgress createDefault() {
        ActivityType activityType = getDefaultActivity();
        return new ActivityProgress(activityType);
    }

    public void reset() {
        this.updateForLevel = false;
        this.successCounter = 0;
        this.mistakeCount = 0;
        this.enemyAnswerCounter = 0;
        this.enemyPointCounter = 0;
        this.playerPointCounter = 0;
        this.previousWord = null;
        this.requiredUserReaction = null;
        this.usedWords = new HashSet<>();
    }

    public int getAmountOfGameInRow() {
        return amountOfGameInRow;
    }

    public void setAmountOfGameInRow(int amountOfGameInRow) {
        this.amountOfGameInRow = amountOfGameInRow;
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

    public int getEnemyPointCounter() {
        return enemyPointCounter;
    }

    public void setEnemyPointCounter(int enemyPointCounter) {
        this.enemyPointCounter = enemyPointCounter;
    }

    public int getPlayerPointCounter() {
        return playerPointCounter;
    }

    public void setPlayerPointCounter(int playerPointCounter) {
        this.playerPointCounter = playerPointCounter;
    }

    public int getEnemyAnswerCounter() {
        return enemyAnswerCounter;
    }

    public void setEnemyAnswerCounter(int enemyAnswerCounter) {
        this.enemyAnswerCounter = enemyAnswerCounter;
    }

    public void iterateEnemyAnswerCounter() {
        this.enemyAnswerCounter += 1;
    }

    public void iterateSuccessAnswerCounter() {
        this.successCounter += 1;
    }

    public void iterateEnemyPointCounter() {
        this.enemyPointCounter += 1;
        this.mistakeCount = 0;
        this.updateForLevel = false;
    }

    public void iteratePlayerPointCounter() {
        this.playerPointCounter += 1;
        this.successCounter = 0;
        this.updateForLevel = false;
    }

    public void iterateAmountOfGameInRow() {
        this.amountOfGameInRow += 1;
    }

    public void iterateEnemyGameCounter() {
        this.enemyGameCounter += 1;
        this.playerGameWinInRow += 0;
        this.enemyGameWinInRow += 1;
    }

    public void iteratePlayerGameCounter() {
        this.playerGameCounter += 1;
        this.playerGameWinInRow += 1;
        this.enemyGameWinInRow = 0;
    }

    public void iterateMistakeCount() {
        this.mistakeCount += 1;
    }

    public int getPlayerGameWinInRow() {
        return playerGameWinInRow;
    }

    public void setPlayerGameWinInRow(int playerGameWinInRow) {
        this.playerGameWinInRow = playerGameWinInRow;
    }

    public int getEnemyGameWinInRow() {
        return enemyGameWinInRow;
    }

    public void setEnemyGameWinInRow(int enemyGameWinInRow) {
        this.enemyGameWinInRow = enemyGameWinInRow;
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

    public boolean isUpdateForLevel() {
        return updateForLevel;
    }

    public void setUpdateForLevel(boolean updateForLevel) {
        this.updateForLevel = updateForLevel;
    }

    public int getMistakeCount() {
        return mistakeCount;
    }

    public void setMistakeCount(int mistakeCount) {
        this.mistakeCount = mistakeCount;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public Set<String> getUsedWords() {
        return usedWords != null ? usedWords : Collections.emptySet();
    }

    public void setUsedWords(String[] words) {
        this.usedWords = new HashSet<>(Arrays.asList(words));
    }

    public void addUsedWord(String word) {
        this.usedWords.add(word);
    }

    public int getPlayerGameCounter() {
        return playerGameCounter;
    }

    public void setPlayerGameCounter(int playerGameCounter) {
        this.playerGameCounter = playerGameCounter;
    }

    public int getEnemyGameCounter() {
        return enemyGameCounter;
    }

    public void setEnemyGameCounter(int enemyGameCounter) {
        this.enemyGameCounter = enemyGameCounter;
    }

    public Set<ActivityType> getUnlockedActivities() {
        return unlockedActivities;
    }

    public void setUnlockedActivities(ActivityType[] unlockedActivities) {
        this.unlockedActivities = new HashSet<>(Arrays.asList(unlockedActivities));
    }

    public void addUnlockedActivity(ActivityType nextActivity) {
        this.unlockedActivities.add(nextActivity);
    }

    public ActivityType getPossibleActivity() {
        return possibleActivity;
    }

    public void setPossibleActivity(ActivityType possibleActivity) {
        this.possibleActivity = possibleActivity;
    }

    public void updateWithDifficultSettings(ActivitySettings settingsForActivity) {
        this.updateForLevel = true;
        this.enemyAnswerCounter = 0;
        if (this.playerPointCounter < settingsForActivity.getIterateComplexityEveryScoresValue()) {
            this.complexity = settingsForActivity.getStartComplexityValue();
        }
        else {
            int multiplication = this.playerPointCounter / settingsForActivity.getIterateComplexityEveryScoresValue();
            multiplication = multiplication * settingsForActivity.getAddToComplexityValue();
            this.complexity = settingsForActivity.getStartComplexityValue() + multiplication;
        }
    }

    @Override
    public String toString() {
        return "ActivityProgress{" +
                "currentActivity=" + currentActivity +
                ", previousActivity=" + previousActivity +
                ", unlockedActivities=" + unlockedActivities +
                ", successCounter=" + successCounter +
                ", enemyAnswerCounter=" + enemyAnswerCounter +
                ", enemyPointCounter=" + enemyPointCounter +
                ", playerPointCounter=" + playerPointCounter +
                ", playerGameCounter=" + playerGameCounter +
                ", enemyGameCounter=" + enemyGameCounter +
                ", playerGameWinInRow=" + playerGameWinInRow +
                ", enemyGameWinInRow=" + enemyGameWinInRow +
                ", complexity=" + complexity +
                ", possibleActivity=" + possibleActivity +
                '}';
    }
}
