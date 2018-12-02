package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityPhrases {

    private List<BasePhraseContainer> intro = Collections.emptyList();
    private List<BasePhraseContainer> opponentFirstPhrase = Collections.emptyList();
    private List<BasePhraseContainer> opponentAfterWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> opponentReactionAfterXWordsPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerLoseWrongWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerLoseRepeatWordPhrase = Collections.emptyList();
    private List<BasePhraseContainer> playerWonOnceAtGame = Collections.emptyList();

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

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentAfterWordPhrase() {
        if (opponentAfterWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(opponentAfterWordPhrase.size() * 2);
        if (index > opponentAfterWordPhrase.size() - 1) {
            return BasePhraseContainer.empty();
        }
        else {
            return opponentAfterWordPhrase.get(index);
        }
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentFirstPhrase() {
        if (opponentFirstPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(opponentFirstPhrase.size());
        return opponentFirstPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentReactionAfterXWordsPhrase() {
        if (opponentReactionAfterXWordsPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(opponentReactionAfterXWordsPhrase.size() * 3);
        if (index > opponentReactionAfterXWordsPhrase.size() - 1) {
            return BasePhraseContainer.empty();
        }
        else {
            return opponentReactionAfterXWordsPhrase.get(index);
        }
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseWrongWordPhrase() {
        if (playerLoseWrongWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(playerLoseWrongWordPhrase.size());
        return playerLoseWrongWordPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseRepeatWordPhrase() {
        if (playerLoseRepeatWordPhrase.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(playerLoseRepeatWordPhrase.size());
        return playerLoseRepeatWordPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerWonOnceAtGamePhrase() {
        if (playerWonOnceAtGame.isEmpty()) {
            return BasePhraseContainer.empty();
        }
        int index = getRandomValue(playerWonOnceAtGame.size());
        return playerWonOnceAtGame.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}
