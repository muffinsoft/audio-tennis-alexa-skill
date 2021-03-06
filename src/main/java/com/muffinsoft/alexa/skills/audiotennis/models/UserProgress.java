package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.skills.audiotennis.enums.ActivityType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserProgress {

    private int wins;
    private int losses;
    private int lastGamePlayerPoint;
    private int lastGameEnemyPoint;
    private int lastGameHistoryPlayerPoint;
    private int lastGameHistoryEnemyPoint;
    private int playerPointWinInRow;
    private int enemyPointWinInRow;
    private int amountOfTwoPointsInRow;
    private Set<String> unlockedActivities = new HashSet<>();
    private Set<String> achievements = new HashSet<>();
    private int nickNameLevel;
    private boolean moveToUpSell = false;
    private int enemyScoresBeforeUpSell;
    private int enemyPointsBeforeUpSell;
    private int playerScoresBeforeUpSell;
    private int playerPointsBeforeUpSell;
    private Set<String> alreadyPlayed = new HashSet<>();

    public UserProgress() {
    }

    public UserProgress(ActivityType unlockedActivity) {
        if (unlockedActivity != null) {
            this.unlockedActivities.add(unlockedActivity.name());
        }
    }

    public int getLastGameHistoryPlayerPoint() {
        return lastGameHistoryPlayerPoint;
    }

    public void setLastGameHistoryPlayerPoint(int lastGameHistoryPlayerPoint) {
        this.lastGameHistoryPlayerPoint = lastGameHistoryPlayerPoint;
    }

    public int getLastGameHistoryEnemyPoint() {
        return lastGameHistoryEnemyPoint;
    }

    public void setLastGameHistoryEnemyPoint(int lastGameHistoryEnemyPoint) {
        this.lastGameHistoryEnemyPoint = lastGameHistoryEnemyPoint;
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

    public int getLastGamePlayerPoint() {
        return lastGamePlayerPoint;
    }

    public void setLastGamePlayerPoint(int lastGamePlayerPoint) {
        this.lastGamePlayerPoint = lastGamePlayerPoint;
        this.lastGameHistoryPlayerPoint = lastGamePlayerPoint;
    }

    public int getLastGameEnemyPoint() {
        return lastGameEnemyPoint;
    }

    public void setLastGameEnemyPoint(int lastGameEnemyPoint) {
        this.lastGameEnemyPoint = lastGameEnemyPoint;
        this.lastGameHistoryEnemyPoint = lastGameEnemyPoint;
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

    public void iterateLoseCounter() {
        this.losses += 1;
    }

    public void iterateWinCounter() {
        this.wins += 1;
    }

    public void addUnlockedActivity(ActivityType activity) {
        this.unlockedActivities.add(activity.name());
    }

    public int getNickNameLevel() {
        return nickNameLevel;
    }

    public void setNickNameLevel(int nickNameLevel) {
        this.nickNameLevel = nickNameLevel;
    }

    public int getAmountOfTwoPointsInRow() {
        return amountOfTwoPointsInRow;
    }

    public void setAmountOfTwoPointsInRow(int amountOfTwoPointsInRow) {
        this.amountOfTwoPointsInRow = amountOfTwoPointsInRow;
    }

    public boolean isMoveToUpSell() {
        return moveToUpSell;
    }

    public void setMoveToUpSell(boolean moveToUpSell) {
        this.moveToUpSell = moveToUpSell;
    }

    public int getEnemyScoresBeforeUpSell() {
        return enemyScoresBeforeUpSell;
    }

    public void setEnemyScoresBeforeUpSell(int enemyScoresBeforeUpSell) {
        this.enemyScoresBeforeUpSell = enemyScoresBeforeUpSell;
    }

    public int getEnemyPointsBeforeUpSell() {
        return enemyPointsBeforeUpSell;
    }

    public void setEnemyPointsBeforeUpSell(int enemyPointsBeforeUpSell) {
        this.enemyPointsBeforeUpSell = enemyPointsBeforeUpSell;
    }

    public int getPlayerScoresBeforeUpSell() {
        return playerScoresBeforeUpSell;
    }

    public void setPlayerScoresBeforeUpSell(int playerScoresBeforeUpSell) {
        this.playerScoresBeforeUpSell = playerScoresBeforeUpSell;
    }

    public int getPlayerPointsBeforeUpSell() {
        return playerPointsBeforeUpSell;
    }

    public void setPlayerPointsBeforeUpSell(int playerPointsBeforeUpSell) {
        this.playerPointsBeforeUpSell = playerPointsBeforeUpSell;
    }

    public Set<String> getAlreadyPlayed() {
        return alreadyPlayed;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "UserProgress{" +
                "wins=" + wins +
                ", losses=" + losses +
                ", lastGamePlayerPoint=" + lastGamePlayerPoint +
                ", lastGameEnemyPoint=" + lastGameEnemyPoint +
                ", lastGameHistoryPlayerPoint=" + lastGameHistoryPlayerPoint +
                ", lastGameHistoryEnemyPoint=" + lastGameHistoryEnemyPoint +
                ", playerPointWinInRow=" + playerPointWinInRow +
                ", enemyPointWinInRow=" + enemyPointWinInRow +
                ", amountOfTwoPointsInRow=" + amountOfTwoPointsInRow +
                ", unlockedActivities=" + unlockedActivities +
                ", achievements=" + achievements +
                ", nickNameLevel=" + nickNameLevel +
                ", moveToUpSell=" + moveToUpSell +
                ", enemyScoresBeforeUpSell=" + enemyScoresBeforeUpSell +
                ", enemyPointsBeforeUpSell=" + enemyPointsBeforeUpSell +
                ", playerScoresBeforeUpSell=" + playerScoresBeforeUpSell +
                ", playerPointsBeforeUpSell=" + playerPointsBeforeUpSell +
                '}';
    }

    public void resetRound() {
        this.lastGamePlayerPoint = 0;
        this.lastGameEnemyPoint = 0;
        this.playerPointWinInRow = 0;
        this.enemyPointWinInRow = 0;
        this.amountOfTwoPointsInRow = 0;
    }
}
