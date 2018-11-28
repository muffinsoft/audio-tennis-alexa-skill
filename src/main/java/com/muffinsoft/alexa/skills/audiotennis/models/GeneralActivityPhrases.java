package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneralActivityPhrases {

    private List<BasePhraseContainer> playerWonOnceAtFirstOrThirdAct;
    private List<BasePhraseContainer> playerWonOnceAtSecondOrFourthAct;
    private List<BasePhraseContainer> enemyWonOnce;
    private List<BasePhraseContainer> playerLoseTwice;
    private List<BasePhraseContainer> playerWonTwice;
    private List<BasePhraseContainer> totalScore;

    public List<BasePhraseContainer> getPlayerWonOnceAtFirstOrThirdAct() {
        return playerWonOnceAtFirstOrThirdAct;
    }

    public void setPlayerWonOnceAtFirstOrThirdAct(List<BasePhraseContainer> playerWonOnceAtFirstOrThirdAct) {
        this.playerWonOnceAtFirstOrThirdAct = playerWonOnceAtFirstOrThirdAct;
    }

    public List<BasePhraseContainer> getPlayerWonOnceAtSecondOrFourthAct() {
        return playerWonOnceAtSecondOrFourthAct;
    }

    public void setPlayerWonOnceAtSecondOrFourthAct(List<BasePhraseContainer> playerWonOnceAtSecondOrFourthAct) {
        this.playerWonOnceAtSecondOrFourthAct = playerWonOnceAtSecondOrFourthAct;
    }

    public List<BasePhraseContainer> getEnemyWonOnce() {
        return enemyWonOnce;
    }

    public void setEnemyWonOnce(List<BasePhraseContainer> enemyWonOnce) {
        this.enemyWonOnce = enemyWonOnce;
    }

    public List<BasePhraseContainer> getPlayerLoseTwice() {
        return playerLoseTwice;
    }

    public void setPlayerLoseTwice(List<BasePhraseContainer> playerLoseTwice) {
        this.playerLoseTwice = playerLoseTwice;
    }

    public List<BasePhraseContainer> getPlayerWonTwice() {
        return playerWonTwice;
    }

    public void setPlayerWonTwice(List<BasePhraseContainer> playerWonTwice) {
        this.playerWonTwice = playerWonTwice;
    }

    public List<BasePhraseContainer> getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(List<BasePhraseContainer> totalScore) {
        this.totalScore = totalScore;
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonOnceAtFirstOrThirdAct() {
        int index = getRandomValue(playerWonOnceAtFirstOrThirdAct.size());
        return playerWonOnceAtFirstOrThirdAct.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonOnceAtSecondOrFourthAct() {
        int index = getRandomValue(playerWonOnceAtSecondOrFourthAct.size());
        return playerWonOnceAtSecondOrFourthAct.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWonOnce() {
        int index = getRandomValue(enemyWonOnce.size());
        return enemyWonOnce.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseTwice() {
        int index = getRandomValue(playerLoseTwice.size());
        return playerLoseTwice.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonTwice() {
        int index = getRandomValue(playerWonTwice.size());
        return playerWonTwice.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomTotalScore() {
        int index = getRandomValue(totalScore.size());
        return totalScore.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}
