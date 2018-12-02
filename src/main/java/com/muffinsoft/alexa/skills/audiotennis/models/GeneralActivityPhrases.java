package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneralActivityPhrases {

    private List<BasePhraseContainer> enemyWonOnce;
    private List<BasePhraseContainer> playerLoseTwice;
    private List<BasePhraseContainer> playerWonTwiceInRow;
    private List<BasePhraseContainer> totalScore;
    private List<BasePhraseContainer> nextRound;
    private List<BasePhraseContainer> playerWinScore;
    private List<BasePhraseContainer> playerWinGame;
    private List<BasePhraseContainer> enemyWinScore;
    private List<BasePhraseContainer> enemyWinGame;
    private List<BasePhraseContainer> defeatPhrase;

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

    public List<BasePhraseContainer> getPlayerWonTwiceInRow() {
        return playerWonTwiceInRow;
    }

    public void setPlayerWonTwiceInRow(List<BasePhraseContainer> playerWonTwiceInRow) {
        this.playerWonTwiceInRow = playerWonTwiceInRow;
    }

    public List<BasePhraseContainer> getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(List<BasePhraseContainer> totalScore) {
        this.totalScore = totalScore;
    }

    public List<BasePhraseContainer> getNextRound() {
        return nextRound;
    }

    public void setNextRound(List<BasePhraseContainer> nextRound) {
        this.nextRound = nextRound;
    }

    public List<BasePhraseContainer> getPlayerWinScore() {
        return playerWinScore;
    }

    public void setPlayerWinScore(List<BasePhraseContainer> playerWinScore) {
        this.playerWinScore = playerWinScore;
    }

    public List<BasePhraseContainer> getPlayerWinGame() {
        return playerWinGame;
    }

    public void setPlayerWinGame(List<BasePhraseContainer> playerWinGame) {
        this.playerWinGame = playerWinGame;
    }

    public List<BasePhraseContainer> getEnemyWinScore() {
        return enemyWinScore;
    }

    public void setEnemyWinScore(List<BasePhraseContainer> enemyWinScore) {
        this.enemyWinScore = enemyWinScore;
    }

    public List<BasePhraseContainer> getEnemyWinGame() {
        return enemyWinGame;
    }

    public void setEnemyWinGame(List<BasePhraseContainer> enemyWinGame) {
        this.enemyWinGame = enemyWinGame;
    }

    public List<BasePhraseContainer> getDefeatPhrase() {
        return defeatPhrase;
    }

    public void setDefeatPhrase(List<BasePhraseContainer> defeatPhrase) {
        this.defeatPhrase = defeatPhrase;
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
    public BasePhraseContainer getRandomNextRound() {
        int index = getRandomValue(nextRound.size());
        return nextRound.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonTwice() {
        int index = getRandomValue(playerWonTwiceInRow.size());
        return playerWonTwiceInRow.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomTotalScore() {
        int index = getRandomValue(totalScore.size());
        return totalScore.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWinGame() {
        int index = getRandomValue(enemyWinGame.size());
        return enemyWinGame.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWinGame() {
        int index = getRandomValue(playerWinGame.size());
        return playerWinGame.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWinScore() {
        int index = getRandomValue(enemyWinScore.size());
        return enemyWinScore.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWinScore() {
        int index = getRandomValue(playerWinScore.size());
        return playerWinScore.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}