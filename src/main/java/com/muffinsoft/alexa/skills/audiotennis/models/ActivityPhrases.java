package com.muffinsoft.alexa.skills.audiotennis.models;

import com.muffinsoft.alexa.sdk.model.BasePhraseContainer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityPhrases {

    private List<BasePhraseContainer> intro;
    private List<BasePhraseContainer> opponentFirstPhrase;
    private List<BasePhraseContainer> opponentAfterWordPhrase;
    private List<BasePhraseContainer> opponentReactionAfterXWordsPhrase;

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

    public BasePhraseContainer getRandomOpponentFirstPhrase() {
        int index = getRandomValue(opponentFirstPhrase.size());
        return opponentFirstPhrase.get(index);
    }

    public BasePhraseContainer getRandomOpponentReactionAfterXWordsPhrasePhrase() {
        int index = getRandomValue(opponentReactionAfterXWordsPhrase.size());
        return opponentReactionAfterXWordsPhrase.get(index);
    }

    public List<BasePhraseContainer> getOpponentAfterWordPhrase() {
        return opponentAfterWordPhrase;
    }

    public void setOpponentAfterWordPhrase(List<BasePhraseContainer> opponentAfterWordPhrase) {
        this.opponentAfterWordPhrase = opponentAfterWordPhrase;
    }

    public BasePhraseContainer getRandomOpponentAfterWordPhrase() {
        int index = getRandomValue(opponentAfterWordPhrase.size() * 2);
        if (index > opponentAfterWordPhrase.size()) {
            return BasePhraseContainer.empty();
        }
        else {
            return opponentAfterWordPhrase.get(index);
        }
    }

    public List<BasePhraseContainer> getOpponentReactionAfterXWordsPhrase() {
        return opponentReactionAfterXWordsPhrase;
    }

    public void setOpponentReactionAfterXWordsPhrase(List<BasePhraseContainer> opponentReactionAfterXWordsPhrase) {
        this.opponentReactionAfterXWordsPhrase = opponentReactionAfterXWordsPhrase;
    }

    private int getRandomValue(int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(maxValue);
    }
}
