package com.muffinsoft.alexa.skills.audiotennis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityPhrases {

    private List<BasePhraseContainer> intro;
    private List<BasePhraseContainer> opponentFirstPhrase;
    private List<BasePhraseContainer> opponentAfterWordPhrase;
    private List<BasePhraseContainer> opponentReactionAfterXWordsPhrase;
    private List<BasePhraseContainer> playerLoseWrongWordPhrase;
    private List<BasePhraseContainer> playerLoseRepeatWordPhrase;

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

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentAfterWordPhrase() {
        int index = getRandomValue(opponentAfterWordPhrase.size() * 2);
        if (index > opponentAfterWordPhrase.size()) {
            return BasePhraseContainer.empty();
        }
        else {
            return opponentAfterWordPhrase.get(index);
        }
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentFirstPhrase() {
        int index = getRandomValue(opponentFirstPhrase.size());
        return opponentFirstPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomOpponentReactionAfterXWordsPhrase() {
        int index = getRandomValue(opponentReactionAfterXWordsPhrase.size());
        return opponentReactionAfterXWordsPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseWrongWordPhrase() {
        int index = getRandomValue(playerLoseWrongWordPhrase.size());
        return playerLoseWrongWordPhrase.get(index);
    }

    @JsonIgnore
    public BasePhraseContainer getRandomPlayerLoseRepeatWordPhrase() {
        int index = getRandomValue(playerLoseRepeatWordPhrase.size());
        return playerLoseRepeatWordPhrase.get(index);
    }

    @JsonIgnore
    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}
