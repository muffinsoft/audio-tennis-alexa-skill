package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.ArrayList;
import java.util.List;

public class GeneralActivityPhrases extends BasePhrases {

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
    private List<BasePhraseContainer> victoryPhrase;
    private List<BasePhraseContainer> callToCelebrate;
    private List<BasePhraseContainer> promotions;
    private List<BasePhraseContainer> levelUps;

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

    public List<BasePhraseContainer> getVictoryPhrase() {
        return victoryPhrase;
    }

    public void setVictoryPhrase(List<BasePhraseContainer> victoryPhrase) {
        this.victoryPhrase = victoryPhrase;
    }

    public List<BasePhraseContainer> getCallToCelebrate() {
        return callToCelebrate;
    }

    public void setCallToCelebrate(List<BasePhraseContainer> callToCelebrate) {
        this.callToCelebrate = callToCelebrate;
    }

    public List<BasePhraseContainer> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<BasePhraseContainer> promotions) {
        this.promotions = promotions;
    }

    public List<BasePhraseContainer> getLevelUps() {
        return levelUps;
    }

    public void setLevelUps(List<BasePhraseContainer> levelUps) {
        this.levelUps = levelUps;
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWonOnce() {
        List<BasePhraseContainer> values = removeAllPartialElements(enemyWonOnce);
        int index = getRandomValue(enemyWonOnce.size());
        return enemyWonOnce.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseTwice() {
        List<BasePhraseContainer> values = removeAllPartialElements(enemyWonOnce);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomNextRound() {
        List<BasePhraseContainer> values = removeAllPartialElements(nextRound);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonTwice() {
        List<BasePhraseContainer> values = removeAllPartialElements(playerWonTwiceInRow);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomTotalScore() {
        List<BasePhraseContainer> values = removeAllPartialElements(totalScore);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWinGame() {
        List<BasePhraseContainer> values = removeAllPartialElements(enemyWinGame);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWinGame() {
        List<BasePhraseContainer> values = removeAllPartialElements(playerWinGame);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyWinScore() {
        List<BasePhraseContainer> values = removeAllPartialElements(enemyWinScore);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWinScore() {
        List<BasePhraseContainer> values = removeAllPartialElements(playerWinScore);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomVictoryPhrase() {
        List<BasePhraseContainer> values = removeAllPartialElements(victoryPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomDefeatPhrase() {
        List<BasePhraseContainer> values = removeAllPartialElements(defeatPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomCallToCelebrate() {
        List<BasePhraseContainer> values = removeAllPartialElements(callToCelebrate);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPromotions() {
        List<BasePhraseContainer> values = removeAllPartialElements(promotions);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomLevelUps() {
        List<BasePhraseContainer> values = removeAllPartialElements(levelUps);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public List<BasePhraseContainer> getAllEnemyWinScoreBySameKey(String key) {
        List<BasePhraseContainer> resultsList = new ArrayList<>();
        for (BasePhraseContainer container : enemyWinScore) {
            if (container.getAudio().startsWith(key)) {
                resultsList.add(container);
            }
        }
        return resultsList;
    }

}