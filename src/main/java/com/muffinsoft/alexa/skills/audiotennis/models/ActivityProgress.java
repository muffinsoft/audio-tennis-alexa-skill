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
    private int playerPointWinInRow;
    private int enemyPointWinInRow;
    private int amountOfTwoPointsInRow;
    private String previousWord;
    private String requiredUserReaction;

    private int complexity;
    private boolean isNew;
    private boolean isTransition;

    private Set<String> usedWords = new HashSet<>();
    private ActivityType possibleActivity;
    private int currentNickNameLevel;

    private ActivityProgress() {
    }

    public ActivityProgress(ActivityType currentActivity) {
        this(currentActivity, true);
    }

    public ActivityProgress(ActivityType currentActivity, boolean isNew) {
        this.isNew = isNew;
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
        return new ActivityProgress(activityType, true);
    }

    public void reset() {
        this.successCounter = 0;
        this.mistakeCount = 0;
        this.enemyAnswerCounter = 0;
        this.previousWord = null;
        this.requiredUserReaction = null;
        this.usedWords = new HashSet<>();
        if (!isTransition) {
            this.playerPointCounter = 0;
            this.enemyPointCounter = 0;
        }
        this.isTransition = false;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public void setTransition(boolean transition) {
        isTransition = transition;
    }

    public int getAmountOfTwoPointsInRow() {
        return amountOfTwoPointsInRow;
    }

    public void setAmountOfTwoPointsInRow(int amountOfTwoPointsInRow) {
        this.amountOfTwoPointsInRow = amountOfTwoPointsInRow;
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
        this.enemyPointWinInRow += 1;
        this.playerPointWinInRow = 0;
        this.mistakeCount = 0;
    }

    public void iteratePlayerPointCounter() {
        this.playerPointCounter += 1;
        this.playerPointWinInRow += 1;
        this.enemyPointWinInRow = 0;
        this.successCounter = 0;
    }

    public void iterateAmountOfTwoPointInRow() {
        this.amountOfTwoPointsInRow += 1;
        this.enemyPointWinInRow = 0;
        this.playerPointWinInRow = 0;
    }

    public void iterateEnemyGameCounter() {
        this.enemyGameCounter += 1;
    }

    public void iteratePlayerGameCounter() {
        this.playerGameCounter += 1;
    }

    public void iterateMistakeCount() {
        this.mistakeCount += 1;
    }

    public int getPlayerPointWinInRow() {
        return playerPointWinInRow;
    }

    public void setPlayerPointWinInRow(int playerPointWinInRow) {
        this.playerPointWinInRow = playerPointWinInRow;
    }

    public int getEnemyPointWinInRow() {
        return enemyPointWinInRow;
    }

    public void setEnemyPointWinInRow(int enemyPointWinInRow) {
        this.enemyPointWinInRow = enemyPointWinInRow;
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
        if (words == null) {
            this.usedWords = new HashSet<>();
        }
        else {
            this.usedWords = new HashSet<>(Arrays.asList(words));
        }
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

    @JsonIgnore
    public boolean isTimeToAddNickName() {
        int temp = this.playerGameCounter - 1;
        return temp % 2 == 0;
    }

    @JsonIgnore
    public boolean isTimeToLevelUp(ActivitySettings settingsForActivity) {
        if (this.playerGameCounter < settingsForActivity.getStartIterationIndex()) {
            return false;
        }
        else {
            int temp = this.playerGameCounter - 1;
            return temp % 2 == 0;
        }
    }

    public void updateWithDifficultSettings(ActivitySettings settingsForActivity) {
        this.isNew = false;
        if (this.playerGameCounter < settingsForActivity.getStartIterationIndex()) {
            this.complexity = settingsForActivity.getStartComplexityValue();
        }
        else if (this.playerGameCounter < (settingsForActivity.getStartIterationIndex() + settingsForActivity.getIterateComplexityEveryScoresValue())) {
            this.complexity = settingsForActivity.getStartComplexityValue() + settingsForActivity.getAddToComplexityValue();
        }
        else {
            int multiplication = (this.playerGameCounter - 1) / settingsForActivity.getIterateComplexityEveryScoresValue();
            multiplication = multiplication * settingsForActivity.getAddToComplexityValue();
            this.complexity = settingsForActivity.getStartComplexityValue() + multiplication;
            if (settingsForActivity.getMaxComplexityValue() != 0 && this.complexity > settingsForActivity.getMaxComplexityValue()) {
                this.complexity = settingsForActivity.getMaxComplexityValue();
            }
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
                ", playerPointWinInRow=" + playerPointWinInRow +
                ", enemyPointWinInRow=" + enemyPointWinInRow +
                ", complexity=" + complexity +
                ", possibleActivity=" + possibleActivity +
                '}';
    }

    public void fromUserProgress(UserProgress userProgress) {

//        if (this.playerGameCounter == 0 && userProgress.getWins() != 0) {
        this.playerGameCounter = userProgress.getWins();
//        }
//        if (this.enemyGameCounter == 0 && userProgress.getLosses() != 0) {
        this.enemyGameCounter = userProgress.getLosses();
//        }
//        if (!userProgress.isEndRound()) {
//            if (this.playerPointCounter == 0 && userProgress.getLastGamePlayerPoint() != 0) {
//                this.playerPointCounter = userProgress.getLastGamePlayerPoint();
//            }
//            if (this.enemyPointCounter == 0 && userProgress.getLastGameEnemyPoint() != 0) {
//                this.enemyPointCounter = userProgress.getLastGameEnemyPoint();
//            }
//            userProgress.setEndRound(false);
//        }
//        if (this.enemyPointWinInRow == 0 && userProgress.getEnemyPointWinInRow() != 0) {
        this.enemyPointWinInRow = userProgress.getEnemyPointWinInRow();
//        }
//        if (this.playerPointWinInRow == 0 && userProgress.getPlayerPointWinInRow() != 0) {
        this.playerPointWinInRow = userProgress.getPlayerPointWinInRow();
//        }
//        if (this.currentNickNameLevel == 0 && userProgress.getNickNameLevel() != 0) {
        this.currentNickNameLevel = userProgress.getNickNameLevel();
//        }
//        if (this.amountOfTwoPointsInRow == 0 && userProgress.getAmountOfTwoPointsInRow() != 0) {
//            this.amountOfTwoPointsInRow = userProgress.getAmountOfTwoPointsInRow();
//        }

//        if (this.getUnlockedActivities() == null || this.getUnlockedActivities().isEmpty() || this.getUnlockedActivities().size() == 1) {
//            if (userProgress.getUnlockedActivities() != null && !userProgress.getUnlockedActivities().isEmpty()) {
        for (String activity : userProgress.getUnlockedActivities()) {
            this.addUnlockedActivity(ActivityType.valueOf(activity));
//                }
//            }
        }
        if(userProgress.isMoveToUpSell()) {
            this.playerGameCounter = userProgress.getPlayerScoresBeforeUpSell();
            this.playerPointCounter = userProgress.getPlayerPointsBeforeUpSell();
            this.enemyGameCounter = userProgress.getEnemyScoresBeforeUpSell();
            this.enemyPointCounter = userProgress.getEnemyPointsBeforeUpSell();
            userProgress.setMoveToUpSell(false);
        }
        this.isNew = false;
    }

    public int getCurrentNickNameLevel() {
        return currentNickNameLevel;
    }

    public void setCurrentNickNameLevel(int currentNickNameLevel) {
        this.currentNickNameLevel = currentNickNameLevel;
    }

    public void iterateNickNameCounter() {
        this.currentNickNameLevel += 1;
    }
}