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
    private int enemySuccessCounter;
    private int enemyScoreCounter;
    private int playerScoreCounter;
    private int currentDifficult;
    private int playerWinRoundCounter;
    private int enemyWinRoundCounter;
    private int playerRoundWinInRow;
    private int enemyRoundWinInRow;
    private String previousWord;
    private String requiredUserReaction;

    private int activityEnemyMistakeIterationPointer;

    private boolean updateForLevel;
    private Set<String> usedWords;

    private ActivityProgress() {
    }

    public ActivityProgress(ActivityType currentActivity) {
        this.currentActivity = currentActivity;
    }

    public static ActivityType getDefaultActivity() {
        return IoC.provideProgressManager().getFirstActivity();
    }

    public static ActivityProgress createDefault() {
        ActivityType activityType = getDefaultActivity();
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

    public int getEnemySuccessCounter() {
        return enemySuccessCounter;
    }

    public void setEnemySuccessCounter(int enemySuccessCounter) {
        this.enemySuccessCounter = enemySuccessCounter;
    }

    public void iterateEnemySuccessCounter() {
        this.enemySuccessCounter += 1;
    }

    public void iterateSuccessCounter() {
        this.successCounter += 1;
    }

    public void iterateEnemyScoreCounter() {
        this.enemyScoreCounter += 1;
    }

    public void iteratePlayerScoreCounter() {
        this.playerScoreCounter += 1;
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

    @Override
    public String toString() {
        return "ActivityProgress{" +
                "currentActivity=" + currentActivity +
                ", previousActivity=" + previousActivity +
                ", successCounter=" + successCounter +
                ", enemySuccessCounter=" + enemySuccessCounter +
                ", enemyScoreCounter=" + enemyScoreCounter +
                ", playerScoreCounter=" + playerScoreCounter +
                ", currentDifficult=" + currentDifficult +
                ", playerWinRoundCounter=" + playerWinRoundCounter +
                ", enemyWinRoundCounter=" + enemyWinRoundCounter +
                ", playerRoundWinInRow=" + playerRoundWinInRow +
                ", enemyRoundWinInRow=" + enemyRoundWinInRow +
                ", previousWord='" + previousWord + '\'' +
                ", requiredUserReaction='" + requiredUserReaction + '\'' +
                ", activityEnemyMistakeIterationPointer=" + activityEnemyMistakeIterationPointer +
                ", updateForLevel=" + updateForLevel +
                ", usedWords=" + usedWords +
                '}';
    }
}
