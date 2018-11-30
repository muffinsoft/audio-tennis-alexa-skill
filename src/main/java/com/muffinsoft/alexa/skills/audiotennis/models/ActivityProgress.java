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
    private int enemyScoreCounter;
    private int playerScoreCounter;
    private int playerWinRoundCounter;
    private int enemyWinRoundCounter;
    private int playerRoundWinInRow;
    private int enemyRoundWinInRow;
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
    private static ActivityType getDefaultActivity() {
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
        this.enemyScoreCounter = 0;
        this.playerScoreCounter = 0;
        this.previousWord = null;
        this.requiredUserReaction = null;
        this.usedWords = new HashSet<>();
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

    public int getEnemyScoreCounter() {
        return enemyScoreCounter;
    }

    public void setEnemyScoreCounter(int enemyScoreCounter) {
        this.enemyScoreCounter = enemyScoreCounter;
    }

    public int getPlayerScoreCounter() {
        return playerScoreCounter;
    }

    public void setPlayerScoreCounter(int playerScoreCounter) {
        this.playerScoreCounter = playerScoreCounter;
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

    public void iterateSuccessCounter() {
        this.successCounter += 1;
    }

    public void iterateEnemyScoreCounter() {
        this.enemyScoreCounter += 1;
        this.mistakeCount = 0;
        this.updateForLevel = false;
    }

    public void iteratePlayerScoreCounter() {
        this.playerScoreCounter += 1;
        this.successCounter = 0;
        this.updateForLevel = false;
    }

    public void iterateEnemyWinRoundCounter() {
        this.enemyWinRoundCounter += 1;
        this.playerRoundWinInRow += 0;
        this.enemyRoundWinInRow += 1;
    }

    public void iteratePlayerWinRoundCounter() {
        this.playerWinRoundCounter += 1;
        this.playerRoundWinInRow += 1;
        this.enemyRoundWinInRow = 0;
    }

    public void iterateMistakeCount() {
        this.mistakeCount += 1;
    }

    public int getPlayerRoundWinInRow() {
        return playerRoundWinInRow;
    }

    public void setPlayerRoundWinInRow(int playerRoundWinInRow) {
        this.playerRoundWinInRow = playerRoundWinInRow;
    }

    public int getEnemyRoundWinInRow() {
        return enemyRoundWinInRow;
    }

    public void setEnemyRoundWinInRow(int enemyRoundWinInRow) {
        this.enemyRoundWinInRow = enemyRoundWinInRow;
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

    public int getPlayerWinRoundCounter() {
        return playerWinRoundCounter;
    }

    public void setPlayerWinRoundCounter(int playerWinRoundCounter) {
        this.playerWinRoundCounter = playerWinRoundCounter;
    }

    public int getEnemyWinRoundCounter() {
        return enemyWinRoundCounter;
    }

    public void setEnemyWinRoundCounter(int enemyWinRoundCounter) {
        this.enemyWinRoundCounter = enemyWinRoundCounter;
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
        if (this.playerScoreCounter < settingsForActivity.getIterateComplexityEveryScoresValue()) {
            this.complexity = settingsForActivity.getStartComplexityValue();
        }
        else {
            int multiplication = this.playerScoreCounter / settingsForActivity.getIterateComplexityEveryScoresValue();
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
                ", enemyScoreCounter=" + enemyScoreCounter +
                ", playerScoreCounter=" + playerScoreCounter +
                ", playerWinRoundCounter=" + playerWinRoundCounter +
                ", enemyWinRoundCounter=" + enemyWinRoundCounter +
                ", playerRoundWinInRow=" + playerRoundWinInRow +
                ", enemyRoundWinInRow=" + enemyRoundWinInRow +
                ", complexity=" + complexity +
                ", possibleActivity=" + possibleActivity +
                '}';
    }
}
