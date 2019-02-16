package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityPhrases {

    private List<BasePhraseContainer> intro = Collections.emptyList();
    private List<BasePhraseContainer> opponentFirstPhrase = Collections.emptyList();
    private List<BasePhraseContainer> opponentAfterWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> opponentReactionAfterXWordsPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerLoseWrongWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> enemyLoseWrongWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerLoseRepeatWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> enemyLoseRepeatWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerWonOnceAtGame = Collections.emptyList();
    private List<BasePhraseContainer> playerTurnAfterEnemyMistake = Collections.emptyList();

    public List<BasePhraseContainer> getIntro() {
        return intro;
    }

    public void setIntro(List<BasePhraseContainer> intro) {
        this.intro = intro;
    }

    public List<BasePhraseContainer> getOpponentFirstPhrase() {
        return opponentFirstPhrase;
    }

    public void setOpponentFirstPhrase(List<BasePhraseContainer> opponentFirstPhrase) {
        this.opponentFirstPhrase = opponentFirstPhrase;
    }

    public List<BasePhraseContainer> getOpponentAfterWordPhrase() {
        return opponentAfterWordPhrase;
    }

    public void setOpponentAfterWordPhrase(List<BasePhraseContainer> opponentAfterWordPhrase) {
        this.opponentAfterWordPhrase = opponentAfterWordPhrase;
    }

    public List<BasePhraseContainer> getPlayerLoseWrongWordPhrase() {
        return playerLoseWrongWordPhrase;
    }

    public void setPlayerLoseWrongWordPhrase(List<BasePhraseContainer> playerLoseWrongWordPhrase) {
        this.playerLoseWrongWordPhrase = playerLoseWrongWordPhrase;
    }

    public List<BasePhraseContainer> getPlayerLoseRepeatWordPhrase() {
        return playerLoseRepeatWordPhrase;
    }

    public void setPlayerLoseRepeatWordPhrase(List<BasePhraseContainer> playerLoseRepeatWordPhrase) {
        this.playerLoseRepeatWordPhrase = playerLoseRepeatWordPhrase;
    }

    public List<BasePhraseContainer> getOpponentReactionAfterXWordsPhrase() {
        return opponentReactionAfterXWordsPhrase;
    }

    public void setOpponentReactionAfterXWordsPhrase(List<BasePhraseContainer> opponentReactionAfterXWordsPhrase) {
        this.opponentReactionAfterXWordsPhrase = opponentReactionAfterXWordsPhrase;
    }

    public List<BasePhraseContainer> getPlayerWonOnceAtGame() {
        return playerWonOnceAtGame;
    }

    public void setPlayerWonOnceAtGame(List<BasePhraseContainer> playerWonOnceAtGame) {
        this.playerWonOnceAtGame = playerWonOnceAtGame;
    }

    public List<BasePhraseContainer> getEnemyLoseWrongWordPhrase() {
        return enemyLoseWrongWordPhrase;
    }

    public void setEnemyLoseWrongWordPhrase(List<BasePhraseContainer> enemyLoseWrongWordPhrase) {
        this.enemyLoseWrongWordPhrase = enemyLoseWrongWordPhrase;
    }

    public List<BasePhraseContainer> getEnemyLoseRepeatWordPhrase() {
        return enemyLoseRepeatWordPhrase;
    }

    public void setEnemyLoseRepeatWordPhrase(List<BasePhraseContainer> enemyLoseRepeatWordPhrase) {
        this.enemyLoseRepeatWordPhrase = enemyLoseRepeatWordPhrase;
    }

    public List<BasePhraseContainer> getPlayerTurnAfterEnemyMistake() {
        return playerTurnAfterEnemyMistake;
    }

    public void setPlayerTurnAfterEnemyMistake(List<BasePhraseContainer> playerTurnAfterEnemyMistake) {
        this.playerTurnAfterEnemyMistake = playerTurnAfterEnemyMistake;
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentAfterWordPhrase() {
        if (opponentAfterWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(opponentAfterWordPhrase);
        int index = getRandomValue(values.size() * 2);
        if (index > values.size() - 1) {
            return BasePhraseContainer.empty();
        }
        else {
            return values.get(index);
        }
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentFirstPhrase() {
        if (opponentFirstPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(opponentFirstPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentReactionAfterXWordsPhrase() {
        if (opponentReactionAfterXWordsPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(opponentReactionAfterXWordsPhrase);
        int index = getRandomValue(values.size() * 3);
        if (index > values.size() - 1) {
            return BasePhraseContainer.empty();
        }
        else {
            return values.get(index);
        }
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseWrongWordPhrase() {
        if (playerLoseWrongWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(playerLoseWrongWordPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyLoseWrongWordPhrase() {
        if (enemyLoseWrongWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(enemyLoseWrongWordPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseRepeatWordPhrase() {
        if (playerLoseRepeatWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(playerLoseRepeatWordPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomEnemyLoseRepeatWordPhrase() {
        if (enemyLoseRepeatWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(enemyLoseRepeatWordPhrase);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonOnceAtGamePhrase() {
        if (playerWonOnceAtGame.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(playerWonOnceAtGame);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerTurnAfterEnemyMistake() {
        if (playerTurnAfterEnemyMistake.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        List<BasePhraseContainer> values = removeAllPartialElements(playerTurnAfterEnemyMistake);
        int index = getRandomValue(values.size());
        return values.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }

    @JsonIgnore
    private List<BasePhraseContainer> removeAllPartialElements(List<BasePhraseContainer> initialList) {
        List<BasePhraseContainer> resultList = new ArrayList<>();
        for (BasePhraseContainer container : initialList) {
            if (container.getRole().equals("Audio")) {
                String link = container.getAudio();
                if (link.charAt(link.length() - 3) != '_') {
                    resultList.add(container);
                }
            }
            else {
                resultList.add(container);
            }
        }
        return resultList;
    }
}
